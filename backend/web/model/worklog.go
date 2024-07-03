package model

// SysDic [...]
type SysDic struct {
	ID          int    `gorm:"primaryKey;column:id;type:int(11) AUTO_INCREMENT;not null" json:"id"`
	Description string `gorm:"column:description;type:varchar(100);not null" json:"description"` // 描述
	Pid         int    `gorm:"column:pid;type:int(11)" json:"pid"`                               // 上级id
	Type        int    `gorm:"column:type;type:int(11);not null" json:"type"`                    // 类型
}

func (SysDic) TableName() string {
	return "sys_dic"
}

// UserTable 用户表
//
//nolint:tagliatelle
type UserTable struct {
	ID         int    `gorm:"primaryKey;column:id;type:int(11) AUTO_INCREMENT;not null" json:"-"`
	UserName   string `gorm:"unique;column:user_name;type:varchar(100)" json:"user_name"` // 用户名
	Password   string `gorm:"column:password;type:varchar(512);not null" json:"password"` // 密码
	CreateTime int64  `gorm:"column:create_time;type:bigint(20);not null" json:"create_time"`
}

func (UserTable) TableName() string {
	return "user_table"
}

// WorkContent 工作内容
type WorkContent struct {
	ID      int    `gorm:"primaryKey;column:id;type:int(11) AUTO_INCREMENT;not null" json:"id"`
	Date    int64  `gorm:"column:date;type:bigint(20)" json:"date"`          // 日期
	Type1   int    `gorm:"column:type1;type:int(11);not null" json:"type1"`  // 工作类别
	Type2   int    `gorm:"column:type2;type:int(11);not null" json:"type2"`  // 工作子类
	Content string `gorm:"column:content;type:text;not null" json:"content"` // 工作内容
}

func (WorkContent) TableName() string {
	return "work_content"
}

// WorkContentResp 获取工作内容
//
//nolint:tagliatelle
type WorkContentResp struct {
	ID      int    `json:"id"`
	Date    int64  `json:"date"`     // 日期
	Type1   string `json:"type1"`    // 工作类别
	Type1ID int    `json:"type1_id"` // 工作类别ID
	Type2   string `json:"type2"`    // 工作子类
	Type2ID int    `json:"type2_id"` // 工作子类ID
	Content string `json:"content"`  // 工作内容
}
