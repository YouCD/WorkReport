package utils

import (
	"WorkReport/web/model"
	"fmt"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
	"log"
	"os"
	"time"
)

//定义全局的db对象，我们执行数据库操作主要通过他实现。
var (
	_db *gorm.DB
	err error
)

//包初始化函数，golang特性，每个包初始化的时候会自动执行init函数，这里用来初始化gorm。
func InitDB(DBUser, DBPwd, DBHost, DBPort, DBName string) {
//func init() {
	//DSN := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8mb4&parseTime=True&loc=Local", "root", "P@ssw0rd", "127.0.0.1", "3306", "worklog") // 连接数据库
	DSN := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8mb4&parseTime=True&loc=Local", DBUser, DBPwd, DBHost, DBPort, DBName) // 连接数据库
	newLogger := logger.New(
		log.New(os.Stdout, "\r\n", log.LstdFlags), // io writer
		logger.Config{
			SlowThreshold: time.Second,   // 慢 SQL 阈值
			LogLevel:      logger.Silent, // Log level
			//LogLevel:      logger.Info, // Log level
			Colorful: false, // 禁用彩色打印
		},
	)

	//连接MYSQL, 获得DB类型实例，用于后面的数据库读写操作。
	_db, err = gorm.Open(mysql.Open(DSN), &gorm.Config{
		Logger: newLogger,
	})

	if err != nil {
		panic("连接数据库失败, error=" + err.Error())
	}
	//设置数据库连接池参数

	sqlDB, err := _db.DB()
	if err != nil {
		log.Panic(err)
	}
	sqlDB.SetMaxOpenConns(100) //设置数据库连接池最大连接数
	sqlDB.SetMaxIdleConns(20)  //连接池最大允许的空闲连接数，如果没有sql任务需要执行的连接数大于20，超过的连接会被连接池关闭。
	// 设置每个链接的过期时间
	sqlDB.SetConnMaxLifetime(time.Second * 5)
	err = sqlDB.Ping()
	if err != nil {
		panic(err)
	}

}


//不用担心协程并发使用同样的db对象会共用同一个连接，db对象在调用他的方法的时候会从数据库连接池中获取新的连接
func GetDB() *gorm.DB {
	return _db
}

func InitTables(DBUser, DBPwd, DBHost, DBPort, DBName, username,password string) (err error){
	//创建表
	DSN := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8mb4&parseTime=True&loc=Local", DBUser, DBPwd, DBHost, DBPort, DBName) // 连接数据库
	db, err := gorm.Open(mysql.Open(DSN), &gorm.Config{})
	//db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4").Migrator().CreateTable(&model.UserTable{})
	if err != nil {
		log.Println(err)
		return err
	}
	err = db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4").AutoMigrate(&model.SysDic{}, &model.UserTable{}, &model.WorkContent{})
	if err != nil {
		log.Println(err)
		return err
	}

	//bytes, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	//if err != nil {
	//	log.Println(err)
	//	return err
	//}
	hashPW, err :=PasswordHash(password)
	if err != nil {
		log.Println(err)
		return err
	}

	user := model.UserTable{
		UserName:   username,
		Password:   hashPW,
		CreateTime: time.Now().Unix(),
	}

	err=db.Create(&user).Error
	if err != nil {
		log.Println(err)
		return err
	}
	log.Printf("The default username is %s password is %s", user.UserName, password)
	return nil
}


func PasswordHash(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	return string(bytes), err
}