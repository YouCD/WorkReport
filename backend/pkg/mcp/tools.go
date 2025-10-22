package mcp

import (
	"WorkReport/pkg/llm"
	"WorkReport/pkg/types"
	"WorkReport/web/model"
	"context"
	"encoding/json"
	"errors"
	"time"

	"github.com/mark3labs/mcp-go/mcp"
	"github.com/youcd/toolkit/db"
	"github.com/youcd/toolkit/log"
)

var (
	//listWorkTypes = mcp.NewTool("listWorkTypes",
	//	mcp.WithDescription("列出所有工作类别及其子类"),
	//)
	addWorkLog = mcp.NewTool("addWorkLog", mcp.WithDescription("功能：添加工作日志"),
		mcp.WithString("content", mcp.Description("工作内容")),
	)

	typesCache = map[*model.SysDic][]*model.SysDic{}
)

func typesCacheStr() string {
	var workTypes []*types.ListWorkTypesItem

	for dic, dics := range typesCache {
		var type2 []string
		for _, sysDic := range dics {
			type2 = append(type2, sysDic.Description)
		}
		workTypes = append(workTypes, &types.ListWorkTypesItem{
			Type1: dic.Description,
			Type2: type2,
		})
	}
	marshal, _ := json.Marshal(workTypes)
	return string(marshal)
}
func ListWorkTypes(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
	var workTypes []*types.ListWorkTypesItem
	h := model.SysDicMgr(db.GetDB())
	rows, err := h.GetFromType(1)
	if err != nil {
		log.Error(err)
		return nil, err
	}

	var result []string
	for _, row := range rows {
		result = append(result, row.Description)
		var type2 []*model.SysDic
		err = model.SysDicMgr(db.GetDB()).Where("type =2 and pid = ?", row.ID).Find(&type2).Error
		if err != nil {
			log.Error(err)
			continue
		}

		var descriptions []string

		for _, dic := range type2 {
			descriptions = append(descriptions, dic.Description)
		}

		workTypes = append(workTypes, &types.ListWorkTypesItem{
			Type1: row.Description,
			Type2: descriptions,
		})

		typesCache[row] = type2
	}
	marshal, _ := json.Marshal(workTypes)
	//fmt.Println(string(marshal))
	return mcp.NewToolResultJSON(string(marshal))
}
func AddWorkLog(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
	var c types.Content
	err := request.BindArguments(&c)
	if err != nil {
		return nil, err
	}
	if c.Content == "" {
		return nil, errors.New("content不能为空")
	}
	workTypes, err := llm.WorkTypes(ctx, c.Content, typesCacheStr())
	if err != nil {
		log.Error(err)
		return nil, err
	}

	var type1, type2 int
	for type1obj, type2objs := range typesCache {
		if type1obj.Description == workTypes.Type1 {
			type1 = type1obj.ID
			for _, dic := range type2objs {
				if dic.Description == workTypes.Type2 {
					type2 = dic.ID
					break
				}
			}
		}
	}
	if type1 == 0 || type2 == 0 {
		return nil, errors.New("未找到对应的工作类别")
	}

	workContent := model.WorkContent{
		Type1:   type1,
		Type2:   type2,
		Content: workTypes.Content,
		Date:    time.Now().Unix(),
	}
	h := model.WorkContentMgr(db.GetDB())
	err = h.Create(&workContent).Error
	if err != nil {
		return nil, err
	}
	return mcp.NewToolResultText("添加成功"), nil
}
