package weblib

import (
	"bytes"
	"encoding/json"
	"github.com/xuri/excelize/v2"
	"github.com/youcd/toolkit/db"
	"io"
	"log"
	"net/http"
	"strconv"
	"time"

	"WorkReport/web/model"
	"WorkReport/web/model/utils"
	"github.com/gin-gonic/gin"
)

type ResourceData struct {
	Code int         `json:"code"`
	Msg  string      `json:"msg"`
	Flag bool        `json:"flag"`
	Data interface{} `json:"data"`
}

//nolint:revive
type ErrToMsgStruct struct {
	Id     string
	Code   int
	Detail string
	Status string
}

type WorkLog struct {
	Date    int64  `json:"date"`
	Type1   string `json:"type1"`
	Type2   string `json:"type2"`
	Content string `json:"content"`
}

func NewEmptyDataSuccessResponse(msg string) *ResourceData {
	return &ResourceData{
		Code: http.StatusOK,
		Msg:  msg,
		Flag: true,
		Data: struct{}{},
	}
}
func NewSuccessResponse(msg string, data interface{}) *ResourceData {
	return &ResourceData{
		Code: http.StatusOK,
		Msg:  msg,
		Flag: true,
		Data: data,
	}
}
func NewEmptyDataErrorResponse(msg string) *ResourceData {
	return &ResourceData{
		Code: 1004,
		Msg:  msg,
		Flag: false,
		Data: struct{}{},
	}
}

func ErrToMsg(err error) string {
	var msgStruct = ErrToMsgStruct{}
	var data = []byte(err.Error())
	//nolint:musttag
	if err = json.Unmarshal(data, &msgStruct); err != nil {
		log.Println(err)
	}

	return msgStruct.Detail
}

//nolint:tagliatelle
type TypeList struct {
	TypeList []*model.SysDic `json:"type_list"`
}

//nolint:tagliatelle
type WorkContentRespList struct {
	WorkContentRespList []*model.WorkContentResp `json:"work_content_resp_list"`
	Sum                 int                      `json:"sum"`
}

func addWorkLog(ctx *gin.Context) {
	data, err := io.ReadAll(ctx.Request.Body)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	var workContent model.WorkContent

	err = json.Unmarshal(data, &workContent)
	if err != nil {
		ctx.JSON(200, NewEmptyDataErrorResponse(err.Error()))
		return
	}

	h := model.WorkContentMgr(db.GetDB())
	err = h.Create(&workContent).Error
	if err != nil {
		ctx.JSON(200, NewEmptyDataErrorResponse(err.Error()))
		return
	}

	ctx.JSON(200, NewEmptyDataSuccessResponse("添加成功"))
}
func getWorkLog(ctx *gin.Context) {
	PageIndex := utils.StrToInt(ctx.Query("pageIndex"))
	PageSize := utils.StrToInt(ctx.Query("pageSize"))
	h := model.WorkContentMgr(db.GetDB())
	result, count := h.Pager(PageIndex, PageSize)
	var tmp WorkContentRespList
	tmp.WorkContentRespList = result
	tmp.Sum = int(count)
	ctx.JSON(200, NewSuccessResponse("获取成功", tmp))
}

func delWorkLog(ctx *gin.Context) {
	id := utils.StrToInt(ctx.Query("id"))
	h := model.WorkContentMgr(db.GetDB())
	var tmp model.WorkContent
	tmp.ID = id
	err := h.Delete(&tmp).Error
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}

	ctx.JSON(200, NewEmptyDataSuccessResponse("删除成功"))
}

func modifyWorkLog(ctx *gin.Context) {
	data, err := io.ReadAll(ctx.Request.Body)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	ctx.Request.Body = io.NopCloser(bytes.NewBuffer(data))
	var workContent model.WorkContent
	err = json.Unmarshal(data, &workContent)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}

	h := model.WorkContentMgr(db.GetDB())
	err = h.Where("id =?", workContent.ID).Updates(map[string]interface{}{"date": workContent.Date, "type1": workContent.Type1, "type2": workContent.Type2, "content": workContent.Content}).Error
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	ctx.JSON(200, NewEmptyDataSuccessResponse("修改成功"))
}

func getWorkType1(ctx *gin.Context) {
	h := model.SysDicMgr(db.GetDB())
	rows, err := h.GetFromType(1)
	if err != nil {
		ctx.JSON(200, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}

	var tmp TypeList
	tmp.TypeList = rows
	ctx.JSON(200, NewSuccessResponse("获取成功", tmp))
}

func getWorkType(ctx *gin.Context) {
	id := utils.StrToInt(ctx.Query("id"))
	h := model.SysDicMgr(db.GetDB())
	rows, err := h.GetFromID(id)
	if err != nil {
		ctx.JSON(200, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	ctx.JSON(200, NewSuccessResponse("获取成功", rows))
}

func editWorkType(ctx *gin.Context) {
	data, err := io.ReadAll(ctx.Request.Body)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	ctx.Request.Body = io.NopCloser(bytes.NewBuffer(data))
	var sysDic model.SysDic

	err = json.Unmarshal(data, &sysDic)
	if err != nil {
		ctx.JSON(200, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	h := model.SysDicMgr(db.GetDB())
	err = h.Where("id =?", sysDic.ID).Updates(map[string]interface{}{"pid": sysDic.Pid, "type": sysDic.Type, "description": sysDic.Description}).Error
	if err != nil {
		ctx.JSON(200, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	ctx.JSON(200, NewEmptyDataSuccessResponse("修改成功"))
}

func addWorkType(ctx *gin.Context) {
	data, err := io.ReadAll(ctx.Request.Body)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	ctx.Request.Body = io.NopCloser(bytes.NewBuffer(data))
	var dic model.SysDic
	err = json.Unmarshal(data, &dic)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))

		return
	}
	h := model.SysDicMgr(db.GetDB())
	err = h.Create(&dic).Error
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))

		return
	}
	ctx.JSON(200, NewEmptyDataSuccessResponse("添加成功"))
}

func getWorkType2(ctx *gin.Context) {
	h := model.SysDicMgr(db.GetDB())
	var tmp TypeList

	err := h.Where("type =2 and pid = ?", utils.StrToInt32(ctx.Query("pid"))).Scan(&tmp.TypeList).Error
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))

		return
	}

	ctx.JSON(200, NewSuccessResponse("获取成功", tmp))
}

func getWorkLogFromType(ctx *gin.Context) {
	PageIndex := utils.StrToInt(ctx.Query("pageIndex"))
	PageSize := utils.StrToInt(ctx.Query("pageSize"))
	typeID := utils.StrToInt(ctx.Query("typeID"))
	h := model.WorkContentMgr(db.GetDB())
	result, count := h.PagerFromType(typeID, PageIndex, PageSize)
	var tmp WorkContentRespList
	tmp.WorkContentRespList = result
	tmp.Sum = int(count)
	ctx.JSON(200, NewSuccessResponse("获取成功", tmp))
}

// 获取本周周一的日期
func GetFirstDateOfWeek() int64 {
	now := time.Now()

	offset := int(time.Monday - now.Weekday())
	if offset > 0 {
		offset = -6
	}

	weekStartDate := time.Date(now.Year(), now.Month(), now.Day(), 0, 0, 0, 0, time.Local).AddDate(0, 0, offset)
	//weekMonday = weekStartDate.Format("2006-01-02")
	return weekStartDate.Unix()
}
func getWorkLogFromWeek(ctx *gin.Context) {
	h := model.WorkContentMgr(db.GetDB())
	result, _ := h.PagerFromWeek(GetFirstDateOfWeek(), GetFirstDateOfWeek()+604799)
	r := make(map[string][]string, 0)
	for _, v := range result {
		r[v.Type1] = append(r[v.Type1], v.Content)
	}

	ctx.JSON(200, NewSuccessResponse("获取成功", r))
}

func getWorkLogFromContent(ctx *gin.Context) {
	content := ctx.Query("content")
	h := model.WorkContentMgr(db.GetDB())
	result, count := h.PagerFromContent(content)
	var tmp WorkContentRespList
	tmp.WorkContentRespList = result
	tmp.Sum = int(count)

	ctx.JSON(200, NewSuccessResponse("获取成功", tmp))
}

func getWorkLogFromDate(ctx *gin.Context) {
	date := utils.Str2int64(ctx.Query("date"))
	h := model.WorkContentMgr(db.GetDB())
	result, count := h.PagerFromDate(date)
	var tmp WorkContentRespList
	tmp.WorkContentRespList = result
	tmp.Sum = int(count)
	ctx.JSON(200, NewSuccessResponse("获取成功", tmp))
}

func gettype1Count(ctx *gin.Context) {
	type CountType1 struct {
		Count int    `json:"count"`
		Type1 string `json:"type1"`
	}

	CountType1List := make([]CountType1, 0)
	h := model.WorkContentMgr(db.GetDB())
	err := h.Select("count(type1) as `Count`,sys_dic.description as Type1").Joins("left join sys_dic on work_content.type1=sys_dic.id").Group("type1").Order("`Count` desc").Scan(&CountType1List).Error
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))

		return
	}
	type respCountType1 struct {
		CountType1Data []CountType1 `json:"countType1Data"`
	}
	var sp respCountType1
	sp.CountType1Data = CountType1List
	ctx.JSON(200, NewSuccessResponse("获取成功", sp))
}

func gettype2Count(ctx *gin.Context) {
	type1ID := utils.Str2int64(ctx.Query("id"))

	type CountType2 struct {
		Count int    `json:"count"`
		Type2 string `json:"type2"`
	}
	CountType2List := make([]CountType2, 0)
	h := model.WorkContentMgr(db.GetDB())
	err := h.Select("count(type2) as `Count`,sys_dic.description as Type2").Joins("left join sys_dic on work_content.type2=sys_dic.id").Where("type1 =?", type1ID).Group("type2").Order("`Count` desc").Scan(&CountType2List).Error
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))

		return
	}

	type respCountType2 struct {
		CountType2Data []CountType2 `json:"countType2Data"`
	}
	var sp respCountType2
	sp.CountType2Data = CountType2List

	ctx.JSON(200, NewSuccessResponse("获取成功", sp))
}

func downloadWorklog(ctx *gin.Context) {
	dateStart := utils.Str2int64(ctx.Query("dateStart"))
	dateEnd := utils.Str2int64(ctx.Query("dateEnd"))

	workLogList := make([]WorkLog, 0)
	h := model.WorkContentMgr(db.GetDB())
	err := h.Select(" work_content.date,type1.description as type1,type2.description as type2,work_content.content").Where("date >=? and date <= ? ", dateStart, dateEnd).Joins("left JOIN sys_dic type1 ON work_content.type1=type1.id LEFT JOIN sys_dic type2 ON work_content.type2 =type2.id").Scan(&workLogList).Error
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	f := excelize.NewFile()
	// Create a new sheet.
	Sheet, err := f.NewSheet("Sheet1")
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}

	err = f.SetCellValue("Sheet1", "A1", "日期")
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	if err = f.SetCellValue("Sheet1", "B1", "工作大类"); err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	if err = f.SetCellValue("Sheet1", "C1", "工作大类"); err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	if err = f.SetCellValue("Sheet1", "D1", "工作类容"); err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}

	for i, v := range workLogList {
		i += 2
		timeTemplate := "2006-01-02"
		// Set value of a cell.
		if err := f.SetCellValue("Sheet1", "A"+strconv.Itoa(i), time.Unix(v.Date, 0).Format(timeTemplate)); err != nil {
			ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
			return
		}
		if err := f.SetCellValue("Sheet1", "B"+strconv.Itoa(i), v.Type1); err != nil {
			ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
			return
		}
		if err := f.SetCellValue("Sheet1", "C"+strconv.Itoa(i), v.Type2); err != nil {
			ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
			return
		}
		if err := f.SetCellValue("Sheet1", "D"+strconv.Itoa(i), v.Content); err != nil {
			ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
			return
		}
	}
	f.SetActiveSheet(Sheet)

	ctx.Header("Content-Type", "application/octet-stream")
	ctx.Header("Content-Disposition", "attachment; filename="+"WorkLog.xlsx")
	ctx.Header("Content-Transfer-Encoding", "binary")
	//回写到web 流媒体 形成下载
	_ = f.Write(ctx.Writer)
}
