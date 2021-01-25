package Weblib

import (
	"bytes"
	"encoding/json"
	"io/ioutil"
	"strconv"
	"time"

	"WorkReport/web/model"
	"WorkReport/web/model/utils"
	"github.com/360EntSecGroup-Skylar/excelize/v2"
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
type WorkLog struct {
	Date    int64  `json:"date"`
	Type1   string `json:"type1"`
	Type2   string `json:"type2"`
	Content string `json:"content"`
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
	h := model.WorkContentMgr(utils.GetDB())
	result, _ := h.PagerFromWeek(GetFirstDateOfWeek(), GetFirstDateOfWeek()+604799)
	r := make(map[string][]string, 0)
	for _, v := range result {
		r[v.Type1] = append(r[v.Type1], v.Content)
	}
	suRsp.Msg = "获取成功"
	suRsp.Data = r
	ctx.JSON(200, suRsp)
}

func getWorkLogFromContent(ctx *gin.Context) {
	content := ctx.Query("content")
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
	date := utils.Str2int64(ctx.Query("date"))
	h := model.WorkContentMgr(utils.GetDB())
	result, count := h.PagerFromDate(date)
	var tmp WorkContentRespList
	tmp.WorkContentRespList = result
	tmp.Sum = int(count)
	suRsp.Msg = "获取成功"
	suRsp.Data = tmp
	ctx.JSON(200, suRsp)
}

func gettype1Count(ctx *gin.Context) {
	type CountType1 struct {
		Count int    `json:"count"`
		Type1 string `json:"type1"`
	}
	CountType1List := make([]CountType1, 0)
	h := model.WorkContentMgr(utils.GetDB())
	err := h.Select("count(type1) as `Count`,sys_dic.description as Type1").Joins("left join sys_dic on work_content.type1=sys_dic.id").Group("type1").Scan(&CountType1List).Error
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(200, errrsp)
		return
	}
	type respCountType1 struct {
		CountList []int    `json:"count_list"`
		Type1List []string `json:"type1_list"`
	}

	var tmp respCountType1
	for _, v := range CountType1List {
		tmp.CountList = append(tmp.CountList, v.Count)
		tmp.Type1List = append(tmp.Type1List, v.Type1)

	}
	suRsp.Msg = "获取成功"
	suRsp.Data = tmp
	ctx.JSON(200, suRsp)
}

func gettype2Count(ctx *gin.Context) {
	type1ID := utils.Str2int64(ctx.Query("id"))

	type CountType2 struct {
		Count int    `json:"count"`
		Type2 string `json:"type2"`
	}
	CountType2List := make([]CountType2, 0)
	h := model.WorkContentMgr(utils.GetDB())
	err := h.Select("count(type2) as `Count`,sys_dic.description as Type2").Joins("left join sys_dic on work_content.type2=sys_dic.id").Where("type1 =?", type1ID).Group("type2").Scan(&CountType2List).Error
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(200, errrsp)
		return
	}
	type respCountType1 struct {
		CountList []int    `json:"count_list"`
		Type2List []string `json:"type2_list"`
	}

	var tmp respCountType1
	for _, v := range CountType2List {
		tmp.CountList = append(tmp.CountList, v.Count)
		tmp.Type2List = append(tmp.Type2List, v.Type2)
	}

	suRsp.Msg = "获取成功"
	suRsp.Data = tmp
	ctx.JSON(200, suRsp)
}

func downloadWorklog(ctx *gin.Context) {
	dateStart := utils.Str2int64(ctx.Query("dateStart"))
	dateEnd := utils.Str2int64(ctx.Query("dateEnd"))

	type CountType2 struct {
		Count int    `json:"count"`
		Type2 string `json:"type2"`
	}
	workLogList := make([]WorkLog, 0)
	h := model.WorkContentMgr(utils.GetDB())
	err := h.Select(" work_content.date,type1.description as type1,type2.description as type2,work_content.content").Where("date >=? and date <= ? ", dateStart, dateEnd).Joins("left JOIN sys_dic type1 ON work_content.type1=type1.id LEFT JOIN sys_dic type2 ON work_content.type2 =type2.id").Scan(&workLogList).Error
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(200, errrsp)
		return
	}
	f := excelize.NewFile()
	// Create a new sheet.
	Sheet := f.NewSheet("Sheet1")
	f.SetCellValue("Sheet1", "A1", "日期")
	f.SetCellValue("Sheet1", "B1", "工作大类")
	f.SetCellValue("Sheet1", "C1", "工作子类")
	f.SetCellValue("Sheet1", "D1", "工作类容")

	for i, v := range workLogList {
		i += 2
		timeTemplate := "2006-01-02"
		// Set value of a cell.
		f.SetCellValue("Sheet1", "A"+strconv.Itoa(i), time.Unix(v.Date, 0).Format(timeTemplate))
		f.SetCellValue("Sheet1", "B"+strconv.Itoa(i), v.Type1)
		f.SetCellValue("Sheet1", "C"+strconv.Itoa(i), v.Type2)
		f.SetCellValue("Sheet1", "D"+strconv.Itoa(i), v.Content)
	}
	// Set active sheet of the workbook.
	f.SetActiveSheet(Sheet)
	// Save spreadsheet by the given path.
	//if err := f.SaveAs("Book1.xlsx"); err != nil {
	//	fmt.Println(err)
	//}

	ctx.Header("Content-Type", "application/octet-stream")
	ctx.Header("Content-Disposition", "attachment; filename="+"WorkLog.xlsx")
	ctx.Header("Content-Transfer-Encoding", "binary")
	//回写到web 流媒体 形成下载
	_ = f.Write(ctx.Writer)
	//suRsp.Msg = "获取成功"
	//ctx.JSON(200, suRsp)
}
