package model

import (
	"context"
	"fmt"
	"gorm.io/gorm"
	"log"
	"os"
)

type _UserTableMgr struct {
	*_BaseMgr
}

// UserTableMgr open func
func UserTableMgr(db *gorm.DB) *_UserTableMgr {
	if db == nil {
		log.Println(fmt.Errorf("UserTableMgr need init by db"))
		os.Exit(1)
	}
	ctx, cancel := context.WithCancel(context.Background())
	return &_UserTableMgr{_BaseMgr: &_BaseMgr{DB: db.Table("user_table"), isRelated: globalIsRelated, ctx: ctx, cancel: cancel, timeout: -1}}
}

// GetTableName get sql table name.获取数据库名字
func (obj *_UserTableMgr) GetTableName() string {
	return "user_table"
}

// Get 获取
func (obj *_UserTableMgr) Get() (result UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Find(&result).Error

	return
}

// Gets 获取批量结果
func (obj *_UserTableMgr) Gets() (results []*UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Find(&results).Error

	return
}

//////////////////////////option case ////////////////////////////////////////////

// WithID id获取
func (obj *_UserTableMgr) WithID(id int) Option {
	return optionFunc(func(o *options) { o.query["id"] = id })
}

// WithUserName user_name获取 用户名
func (obj *_UserTableMgr) WithUserName(userName string) Option {
	return optionFunc(func(o *options) { o.query["user_name"] = userName })
}

// WithPassword password获取 密码
func (obj *_UserTableMgr) WithPassword(password string) Option {
	return optionFunc(func(o *options) { o.query["password"] = password })
}

// WithCreateTime create_time获取
func (obj *_UserTableMgr) WithCreateTime(createTime int64) Option {
	return optionFunc(func(o *options) { o.query["create_time"] = createTime })
}

// GetByOption 功能选项模式获取
func (obj *_UserTableMgr) GetByOption(opts ...Option) (result UserTable, err error) {
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
func (obj *_UserTableMgr) GetByOptions(opts ...Option) (results []*UserTable, err error) {
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
func (obj *_UserTableMgr) GetFromID(id int) (result UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("id = ?", id).Find(&result).Error

	return
}

// GetBatchFromID 批量唯一主键查找
func (obj *_UserTableMgr) GetBatchFromID(ids []int) (results []*UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("id IN (?)", ids).Find(&results).Error

	return
}

// GetFromUserName 通过user_name获取内容 用户名
func (obj *_UserTableMgr) GetFromUserName(userName string) (result UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("user_name = ?", userName).Find(&result).Error

	return
}

// GetBatchFromUserName 批量唯一主键查找 用户名
func (obj *_UserTableMgr) GetBatchFromUserName(userNames []string) (results []*UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("user_name IN (?)", userNames).Find(&results).Error

	return
}

// GetFromPassword 通过password获取内容 密码
func (obj *_UserTableMgr) GetFromPassword(password string) (results []*UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("password = ?", password).Find(&results).Error

	return
}

// GetBatchFromPassword 批量唯一主键查找 密码
func (obj *_UserTableMgr) GetBatchFromPassword(passwords []string) (results []*UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("password IN (?)", passwords).Find(&results).Error

	return
}

// GetFromCreateTime 通过create_time获取内容
func (obj *_UserTableMgr) GetFromCreateTime(createTime int64) (results []*UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("create_time = ?", createTime).Find(&results).Error

	return
}

// GetBatchFromCreateTime 批量唯一主键查找
func (obj *_UserTableMgr) GetBatchFromCreateTime(createTimes []int64) (results []*UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("create_time IN (?)", createTimes).Find(&results).Error

	return
}

//////////////////////////primary index case ////////////////////////////////////////////

// FetchByPrimaryKey primay or index 获取唯一内容
func (obj *_UserTableMgr) FetchByPrimaryKey(id int) (result UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("id = ?", id).Find(&result).Error

	return
}

// FetchUniqueByUserTableUN primay or index 获取唯一内容
func (obj *_UserTableMgr) FetchUniqueByUserTableUN(userName string) (result UserTable, err error) {
	err = obj.DB.WithContext(obj.ctx).Table(obj.GetTableName()).Where("user_name = ?", userName).Find(&result).Error

	return
}
