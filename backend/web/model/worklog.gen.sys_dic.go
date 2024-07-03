package model

import (
	"context"
	"os"

	"github.com/youcd/toolkit/log"
	"gorm.io/gorm"
)

type _SysDicMgr struct {
	*_BaseMgr
}

// SysDicMgr open func
//
//nolint:revive
func SysDicMgr(db *gorm.DB) *_SysDicMgr {
	if db == nil {
		log.Error("SysDicMgr need init by db")
		os.Exit(1)
	}
	ctx, cancel := context.WithCancel(context.Background())
	return &_SysDicMgr{_BaseMgr: &_BaseMgr{DB: db.Table("sys_dic"), isRelated: globalIsRelated, ctx: ctx, cancel: cancel, timeout: -1}}
}

// GetTableName get sql table name.获取数据库名字
func (obj *_SysDicMgr) GetTableName() string {
	return "sys_dic"
}

// Get 获取
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) Get() (result SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Find(&result).Error

	return
}

// Gets 获取批量结果
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) Gets() (results []*SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Find(&results).Error

	return
}

//////////////////////////option case ////////////////////////////////////////////

// WithID id获取
//
//nolint:ireturn
func (obj *_SysDicMgr) WithID(id int) Option {
	return optionFunc(func(o *options) { o.query["id"] = id })
}

// WithDescription description获取 描述
//
//nolint:ireturn
func (obj *_SysDicMgr) WithDescription(description string) Option {
	return optionFunc(func(o *options) { o.query["description"] = description })
}

// WithPid pid获取 上级id
//
//nolint:ireturn
func (obj *_SysDicMgr) WithPid(pid int) Option {
	return optionFunc(func(o *options) { o.query["pid"] = pid })
}

// WithType type获取 类型
//
//nolint:ireturn
func (obj *_SysDicMgr) WithType(_type int) Option {
	return optionFunc(func(o *options) { o.query["type"] = _type })
}

// GetByOption 功能选项模式获取
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) GetByOption(opts ...Option) (result SysDic, err error) {
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
func (obj *_SysDicMgr) GetByOptions(opts ...Option) (results []*SysDic, err error) {
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
func (obj *_SysDicMgr) GetFromID(id int) (result SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("id = ?", id).Find(&result).Error

	return
}

// GetBatchFromID 批量唯一主键查找
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) GetBatchFromID(ids []int) (results []*SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("id IN (?)", ids).Find(&results).Error

	return
}

// GetFromDescription 通过description获取内容 描述
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) GetFromDescription(description string) (results []*SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("description = ?", description).Find(&results).Error

	return
}

// GetBatchFromDescription 批量唯一主键查找 描述
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) GetBatchFromDescription(descriptions []string) (results []*SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("description IN (?)", descriptions).Find(&results).Error

	return
}

// GetFromPid 通过pid获取内容 上级id
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) GetFromPid(pid int) (results []*SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("pid = ?", pid).Find(&results).Error

	return
}

// GetBatchFromPid 批量唯一主键查找 上级id
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) GetBatchFromPid(pids []int) (results []*SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("pid IN (?)", pids).Find(&results).Error

	return
}

// GetFromType 通过type获取内容 类型
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) GetFromType(_type int) (results []*SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("type = ?", _type).Find(&results).Error

	return
}

// GetBatchFromType 批量唯一主键查找 类型
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) GetBatchFromType(_types []int) (results []*SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("type IN (?)", _types).Find(&results).Error

	return
}

//////////////////////////primary index case ////////////////////////////////////////////

// FetchByPrimaryKey primay or index 获取唯一内容
//
//nolint:nonamedreturns
func (obj *_SysDicMgr) FetchByPrimaryKey(id int) (result SysDic, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("id = ?", id).Find(&result).Error

	return
}
