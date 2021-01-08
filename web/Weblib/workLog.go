package Weblib

import (
	"bytes"
	"encoding/json"
	"io/ioutil"
	"time"

	"WorkReport/web/model"
	"WorkReport/web/model/utils"
	"github.com/gin-gonic/gin"
)

type EmptyData struct{}
type ResourceData struct {
	Code int         `json:"code"`
	Msg  string      `json:"msg"`
	Flag bool        `json:"flag"`
	Data interface{} `json:"data"`
}
type ErrToMsgStruct struct {
	Id     string
	Code   int
	Detail string
	Status string
}

var (
	suRsp  = ResourceData{Code: 200, Flag: true}
	errrsp = ResourceData{Code: 1004, Flag: false, Data: EmptyData{}}
)

func ErrToMsg(err error) string {
	var msgStruct = ErrToMsgStruct{}
	var data = []byte(err.Error())
	json.Unmarshal(data, &msgStruct)
	return msgStruct.Detail
}

type TypeList struct {
	TypeList []*model.SysDic `json:"type_list"`
}
type WorkContentRespList struct {
	WorkContentRespList []*model.WorkContentResp `json:"work_content_resp_list"`
	Sum                 int                      `json:"sum"`
}

func addWorkLog(ctx *gin.Context) {
	data, err := ioutil.ReadAll(ctx.Request.Body)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
		return
	}
	ctx.Request.Body = ioutil.NopCloser(bytes.NewBuffer(data))
	var workContent model.WorkContent

	err = json.Unmarshal(data, &workContent)
	if err != nil {
		errrsp.Msg = err.Error()
		ctx.JSON(200, errrsp)
		return
	}

	h := model.WorkContentMgr(utils.GetDB())
	err = h.Create(&workContent).Error
	if err != nil {
		errrsp.Msg = err.Error()
		ctx.JSON(200, errrsp)
		return
	}
	suRsp.Msg = "添加成功"
	ctx.JSON(200, suRsp)
	//}
}
func getWorkLog(ctx *gin.Context) {
	PageIndex := utils.StrToInt(ctx.Query("pageIndex"))
	PageSize := utils.StrToInt(ctx.Query("pageSize"))
	h := model.WorkContentMgr(utils.GetDB())
	result, count := h.Pager(PageIndex, PageSize)
	var tmp WorkContentRespList
	tmp.WorkContentRespList = result
	tmp.Sum = int(count)
	suRsp.Msg = "获取成功"
	suRsp.Data = tmp
	ctx.JSON(200, suRsp)

}

func delWorkLog(ctx *gin.Context) {
	id := utils.StrToInt(ctx.Query("id"))

	h := model.WorkContentMgr(utils.GetDB())
	var tmp model.WorkContent
	tmp.ID = id
	err := h.Delete(&tmp).Error
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
		return
	}

	suRsp.Msg = "删除成功"
	suRsp.Data = tmp
	ctx.JSON(200, suRsp)

}

func modifyWorkLog(ctx *gin.Context) {
	data, err := ioutil.ReadAll(ctx.Request.Body)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
		return
	}
	ctx.Request.Body = ioutil.NopCloser(bytes.NewBuffer(data))
	var workContent model.WorkContent
	err = json.Unmarshal(data, &workContent)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
		return
	}

	h := model.WorkContentMgr(utils.GetDB())
	err = h.Where("id =?", workContent.ID).Updates(map[string]interface{}{"date": workContent.Date, "type1": workContent.Type1, "type2": workContent.Type2, "content": workContent.Content}).Error
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
		return
	}
	suRsp.Msg = "修改成功"
	ctx.JSON(200, suRsp)

}

func getWorkType1(ctx *gin.Context) {
	h := model.SysDicMgr(utils.GetDB())
	rows, err := h.GetFromType(1)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(200, errrsp)
		return
	}

	var tmp TypeList
	tmp.TypeList = rows
	suRsp.Data = tmp
	suRsp.Msg = "获取成功"
	ctx.JSON(200, suRsp)

}

func getWorkType(ctx *gin.Context) {
	id := utils.StrToInt(ctx.Query("id"))
	h := model.SysDicMgr(utils.GetDB())
	rows, err := h.GetFromID(id)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(200, errrsp)
		return
	}
	suRsp.Data = rows
	suRsp.Msg = "获取成功"
	ctx.JSON(200, suRsp)
}

func editWorkType(ctx *gin.Context) {
	data, err := ioutil.ReadAll(ctx.Request.Body)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
		return
	}
	ctx.Request.Body = ioutil.NopCloser(bytes.NewBuffer(data))
	var sysDic model.SysDic

	err = json.Unmarshal(data, &sysDic)
	if err != nil {
		errrsp.Msg = err.Error()
		ctx.JSON(200, errrsp)
		return
	}
	h := model.SysDicMgr(utils.GetDB())
	err = h.Where("id =?", sysDic.ID).Updates(map[string]interface{}{"pid": sysDic.Pid, "type": sysDic.Type, "description": sysDic.Description}).Error
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(200, errrsp)
		return
	}
	suRsp.Msg = "修改成功"
	ctx.JSON(200, suRsp)
}

func addWorkType(ctx *gin.Context) {
	data, err := ioutil.ReadAll(ctx.Request.Body)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
		return
	}
	ctx.Request.Body = ioutil.NopCloser(bytes.NewBuffer(data))
	var dic model.SysDic
	err = json.Unmarshal(data, &dic)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
		return
	}
	h := model.SysDicMgr(utils.GetDB())
	err = h.Create(&dic).Error
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(200, errrsp)
		return
	}
	suRsp.Msg = "添加成功"
	ctx.JSON(200, suRsp)
}

func getWorkType2(ctx *gin.Context) {
	h := model.SysDicMgr(utils.GetDB())
	var tmp TypeList

	err := h.Where("type =2 and pid = ?", utils.StrToInt32(ctx.Query("pid"))).Scan(&tmp.TypeList).Error
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(200, errrsp)
		return
	}

	suRsp.Data = tmp
	suRsp.Msg = "获取成功"
	ctx.JSON(200, suRsp)

}

func getWorkLogFromType(ctx *gin.Context) {
	PageIndex := utils.StrToInt(ctx.Query("pageIndex"))
	PageSize := utils.StrToInt(ctx.Query("pageSize"))
	typeID := utils.StrToInt(ctx.Query("typeID"))
	h := model.WorkContentMgr(utils.GetDB())
	result, count := h.PagerFromType(typeID, PageIndex, PageSize)
	var tmp WorkContentRespList
	tmp.WorkContentRespList = result
	tmp.Sum = int(count)
	suRsp.Msg = "获取成功"
	suRsp.Data = tmp
	ctx.JSON(200, suRsp)
}

//获取本周周一的日期
func GetFirstDateOfWeek()int64 {
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
	h := model.WorkContentMgr(utils.GetDB())
	result, count := h.PagerFromWeek(GetFirstDateOfWeek(),GetFirstDateOfWeek()+604799)
	var tmp WorkContentRespList
	tmp.WorkContentRespList = result
	tmp.Sum = int(count)
	suRsp.Msg = "获取成功"
	suRsp.Data = tmp
	ctx.JSON(200, suRsp)
}

func getWorkLogFromContent(ctx *gin.Context) {
	content :=ctx.Query("content")
	h := model.WorkContentMgr(utils.GetDB())
	result, count := h.PagerFromContent(content)
	var tmp WorkContentRespList
	tmp.WorkContentRespList = result
	tmp.Sum = int(count)
	suRsp.Msg = "获取成功"
	suRsp.Data = tmp
	ctx.JSON(200, suRsp)
}

func getWorkLogFromDate(ctx *gin.Context) {
	date :=utils.Str2int64(ctx.Query("date"))
	h := model.WorkContentMgr(utils.GetDB())
	result, count := h.PagerFromDate(date)
	var tmp WorkContentRespList
	tmp.WorkContentRespList = result
	tmp.Sum = int(count)
	suRsp.Msg = "获取成功"
	suRsp.Data = tmp
	ctx.JSON(200, suRsp)
}

