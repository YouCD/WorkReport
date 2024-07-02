package utils

import (
	"WorkReport/web/model"
	"fmt"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"log"
	"time"
)

// 定义全局的db对象，我们执行数据库操作主要通过他实现。
var (
	_db *gorm.DB
	err error
)

func InitTables(DBUser, DBPwd, DBHost, DBPort, DBName, username, password string) (err error) {
	//创建表
	DSN := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8mb4&parseTime=True&loc=Local", DBUser, DBPwd, DBHost, DBPort, DBName) // 连接数据库
	_db, err = gorm.Open(mysql.Open(DSN), &gorm.Config{})
	//db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4").Migrator().CreateTable(&model.UserTable{})
	if err != nil {
		log.Println(err)
		return err
	}
	err = _db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4").AutoMigrate(&model.SysDic{}, &model.UserTable{}, &model.WorkContent{})
	if err != nil {
		log.Println(err)
		return err
	}

	err = CreateOrUpdateUser(username, password)
	if err != nil {
		log.Println(err)
		return err
	}
	log.Printf("The default username is %s password is %s", username, password)
	return nil
}

func PasswordHash(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	return string(bytes), err
}

func CreateOrUpdateUser(username, password string) (err error) {
	var userModel model.UserTable
	err = _db.Model(&model.UserTable{}).Where("user_name =?", username).Scan(&userModel).Error
	if err != nil {
		log.Println(err)
		return err
	}
	hashPW, err := PasswordHash(password)
	if err != nil {
		log.Println(err)
		return err
	}
	if userModel.UserName == "" {
		user := model.UserTable{
			UserName:   username,
			Password:   hashPW,
			CreateTime: time.Now().Unix(),
		}
		err = _db.Create(&user).Error
		if err != nil {
			log.Println(err)
			return err
		}
	} else if userModel.UserName != "" {
		err = _db.Model(&model.UserTable{}).Where("user_name = ?", username).Update("password", hashPW).Error
		if err != nil {
			log.Println(err)
			return err
		}
	}
	return nil
}
