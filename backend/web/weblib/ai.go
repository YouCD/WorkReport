package weblib

import (
	"WorkReport/internal/config"
	"WorkReport/pkg/email"
	"WorkReport/pkg/llm"
	"WorkReport/pkg/mcp"
	"WorkReport/pkg/tools"
	"WorkReport/pkg/types"
	"WorkReport/web/model"
	"bytes"
	"encoding/json"
	"text/template"

	"io"

	"github.com/gin-gonic/gin"
	m "github.com/mark3labs/mcp-go/mcp"
	"github.com/youcd/toolkit/db"
	"github.com/youcd/toolkit/log"
)

func addContent(ctx *gin.Context) {
	data, err := io.ReadAll(ctx.Request.Body)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}

	var workContent types.Content
	if err = json.Unmarshal(data, &workContent); err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	result, err := mcp.AddWorkLog(ctx, m.CallToolRequest{Params: m.CallToolParams{
		Name:      "addWorkLog",
		Arguments: workContent,
	}})
	if err != nil {
		log.Error("addWorkLog", err)
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	ctx.JSON(200, NewSuccessResponse("", result))
}

func workLogFromWeek(ctx *gin.Context) {
	h := model.WorkContentMgr(db.GetDB())
	result, _ := h.PagerFromWeek(tools.GetFirstDateOfWeek().Unix(), tools.GetFirstDateOfWeek().Unix()+604799)
	r := make(map[string][]string)
	for _, v := range result {
		r["工作大类与子类： "+v.Type1+"-"+v.Type2] = append(r[v.Type1], v.Content)
	}

	jsn, _ := json.Marshal(r)
	log.Debug(string(jsn))
	workLog, err2 := llm.WeekWorkLog(ctx, string(jsn))
	if err2 != nil {
		log.Error("WeekWorkLog", err2)
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err2)))
		return
	}
	log.Debug(workLog)
	funcMap := template.FuncMap{
		"add": func(a, b int) int { return a + b },
	}
	t := template.New("workLog").Funcs(funcMap)

	parse, err := t.Parse(config.Cfg.Email.ContentTpl)
	if err != nil {
		log.Error("parse", err)
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	var buffer bytes.Buffer
	err = parse.ExecuteTemplate(&buffer, "workLog", workLog)
	if err != nil {
		log.Error("executeTemplate", err)
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	ctx.JSON(200, NewSuccessResponse("", buffer.String()))
}

func sendEmail(ctx *gin.Context) {
	data, err := io.ReadAll(ctx.Request.Body)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}

	var workContent types.Content
	if err = json.Unmarshal(data, &workContent); err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	err = email.SenEmail(workContent.Content)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	ctx.JSON(200, NewEmptyDataSuccessResponse("发送成功"))
}
