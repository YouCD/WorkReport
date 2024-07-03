package model

import (
	"context"
	"os"

	"github.com/youcd/toolkit/log"
	"gorm.io/gorm"
)

type _WorkContentMgr struct {
	*_BaseMgr
}

const (
	BaseSQL1 = "left join sys_dic sd on sd.id = work_content.type1 left join sys_dic on sys_dic.id = work_content.type2"
	BaseSQL2 = "work_content.id,work_content.date,work_content.content,sd.description as type1,sd.id as Type1ID,sys_dic.description as type2,sys_dic.id as Type2ID"
)

//nolint:nonamedreturns
func (obj *_WorkContentMgr) Pager(pageIndex, pageSize int) (result []*WorkContentResp, count int64) {
	resp := obj.DB.Table(obj.GetTableName()).Joins(BaseSQL1)
	// 总行数
	resp.Count(&count)
	resp.Select(BaseSQL2).Offset((pageIndex - 1) * pageSize).Limit(pageSize).Order("work_content.date desc").Order("work_content.id desc").Scan(&result)
	return
}

//nolint:nonamedreturns
func (obj *_WorkContentMgr) PagerFromType(typeID, pageIndex, pageSize int) (result []*WorkContentResp, count int64) {
	resp := obj.DB.Table(obj.GetTableName()).Joins(BaseSQL1).Where("type1 = ? or type2 =?", typeID, typeID)

	resp.Count(&count) // 总行数
	resp.Select(BaseSQL2).Offset((pageIndex - 1) * pageSize).Limit(pageSize).Scan(&result)
	return
}

//nolint:nonamedreturns
func (obj *_WorkContentMgr) PagerFromWeek(weekStartDate, weekEndDate int64) (result []*WorkContentResp, count int64) {
	resp := obj.DB.Table(obj.GetTableName()).Joins(BaseSQL1).Where("date >=? and date <=?", weekStartDate, weekEndDate)

	resp.Count(&count) // 总行数
	resp.Select(BaseSQL2).Scan(&result)
	return
}

//nolint:nonamedreturns
func (obj *_WorkContentMgr) PagerFromContent(content string) (result []*WorkContentResp, count int64) {
	resp := obj.DB.Table(obj.GetTableName()).Joins(BaseSQL1).Where("content like ?", "%"+content+"%")

	resp.Count(&count) // 总行数
	resp.Select(BaseSQL2).Scan(&result)
	return
}

//nolint:nonamedreturns
func (obj *_WorkContentMgr) PagerFromDate(date int64) (result []*WorkContentResp, count int64) {
	resp := obj.DB.Table(obj.GetTableName()).Joins(BaseSQL1).Where("date = ?", date)
	resp.Count(&count) // 总行数
	resp.Select(BaseSQL2).Scan(&result)
	return
}

// WorkContentMgr open func
//
//nolint:revive
func WorkContentMgr(db *gorm.DB) *_WorkContentMgr {
	if db == nil {
		log.Error("WorkContentMgr need init by db")
		os.Exit(1)
	}
	ctx, cancel := context.WithCancel(context.Background())
	return &_WorkContentMgr{_BaseMgr: &_BaseMgr{DB: db.Table("work_content"), isRelated: globalIsRelated, ctx: ctx, cancel: cancel, timeout: -1}}
}

// GetTableName get sql table name.获取数据库名字
func (obj *_WorkContentMgr) GetTableName() string {
	return "work_content"
}

// Get 获取
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) Get() (result WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Find(&result).Error

	return
}

// Gets 获取批量结果
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) Gets() (results []*WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Find(&results).Error

	return
}

//////////////////////////option case ////////////////////////////////////////////

// WithID id获取
//
//nolint:ireturn
func (obj *_WorkContentMgr) WithID(id int) Option {
	return optionFunc(func(o *options) { o.query["id"] = id })
}

// WithDate date获取 日期
//
//nolint:ireturn
func (obj *_WorkContentMgr) WithDate(date int64) Option {
	return optionFunc(func(o *options) { o.query["date"] = date })
}

// WithType1 type1获取 工作类别
//
//nolint:ireturn
func (obj *_WorkContentMgr) WithType1(type1 int) Option {
	return optionFunc(func(o *options) { o.query["type1"] = type1 })
}

// WithType2 type2获取 工作子类
//
//nolint:ireturn
func (obj *_WorkContentMgr) WithType2(type2 int) Option {
	return optionFunc(func(o *options) { o.query["type2"] = type2 })
}

// WithContent content获取 工作内容
//
//nolint:ireturn
func (obj *_WorkContentMgr) WithContent(content string) Option {
	return optionFunc(func(o *options) { o.query["content"] = content })
}

// GetByOption 功能选项模式获取
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetByOption(opts ...Option) (result WorkContent, err error) {
	options := options{
		query: make(map[string]interface{}, len(opts)),
	}
	for _, o := range opts {
		o.apply(&options)
	}

	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where(options.query).Find(&result).Error

	return
}

// GetByOptions 批量功能选项模式获取
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetByOptions(opts ...Option) (results []*WorkContent, err error) {
	options := options{
		query: make(map[string]interface{}, len(opts)),
	}
	for _, o := range opts {
		o.apply(&options)
	}

	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where(options.query).Find(&results).Error

	return
}

//////////////////////////enume case ////////////////////////////////////////////

// GetFromID 通过id获取内容
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetFromID(id int) (result WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("id = ?", id).Find(&result).Error

	return
}

// GetBatchFromID 批量唯一主键查找
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetBatchFromID(ids []int) (results []*WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("id IN (?)", ids).Find(&results).Error

	return
}

// GetFromDate 通过date获取内容 日期
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetFromDate(date int64) (results []*WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("date = ?", date).Find(&results).Error

	return
}

// GetBatchFromDate 批量唯一主键查找 日期
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetBatchFromDate(dates []int64) (results []*WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("date IN (?)", dates).Find(&results).Error

	return
}

// GetFromType1 通过type1获取内容 工作类别
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetFromType1(type1 int) (results []*WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("type1 = ?", type1).Find(&results).Error

	return
}

// GetBatchFromType1 批量唯一主键查找 工作类别
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetBatchFromType1(type1s []int) (results []*WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("type1 IN (?)", type1s).Find(&results).Error

	return
}

// GetFromType2 通过type2获取内容 工作子类
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetFromType2(type2 int) (results []*WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("type2 = ?", type2).Find(&results).Error

	return
}

// GetBatchFromType2 批量唯一主键查找 工作子类
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetBatchFromType2(type2s []int) (results []*WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("type2 IN (?)", type2s).Find(&results).Error

	return
}

// GetFromContent 通过content获取内容 工作内容
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetFromContent(content string) (results []*WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("content = ?", content).Find(&results).Error

	return
}

// GetBatchFromContent 批量唯一主键查找 工作内容
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) GetBatchFromContent(contents []string) (results []*WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("content IN (?)", contents).Find(&results).Error

	return
}

//////////////////////////primary index case ////////////////////////////////////////////

// FetchByPrimaryKey primay or index 获取唯一内容
//
//nolint:nonamedreturns
func (obj *_WorkContentMgr) FetchByPrimaryKey(id int) (result WorkContent, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("id = ?", id).Find(&result).Error

	return
}
