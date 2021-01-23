package Weblib

import (
	"WorkReport/common"
	"fmt"
	"github.com/gin-gonic/gin"
	"golang.org/x/crypto/bcrypt"
	"os"
)

var (
	versionInfo = common.ReleaseVersion{}
	updateFlag  bool
	isUpdated   bool
)

func PasswordHash(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	return string(bytes), err
}

func PasswordVerify(password, hash string) bool {
	err := bcrypt.CompareHashAndPassword([]byte(hash), []byte(password))
	return err == nil
}

func UpdateCheck(ctx *gin.Context) {
	if !isUpdated {
		versionInfo = common.GetRelease()
		if common.Version != versionInfo.TagName {
			suRsp.Data = versionInfo
			suRsp.Msg = fmt.Sprintf("有新版本可以更新!  当前版本%s,最新版本%s 点击更新", common.Version, versionInfo.TagName)
			ctx.JSON(200, suRsp)
		} else {
			errrsp.Msg = fmt.Sprintf("已是最新版本%s", common.Version)
			ctx.JSON(200, errrsp)
		}
	} else {
		errrsp.Msg = "更新完成，请重启软件"
		ctx.JSON(200, errrsp)
	}

}
func Update(ctx *gin.Context) {
	method := ctx.Query("method")
	if method == "loop_check" {
		if updateFlag {
			suRsp.Msg = "更新完成，请重启软件"
			ctx.JSON(200, suRsp)
			updateFlag = false
			isUpdated = true
		} else if !updateFlag {
			errrsp.Msg = "更新中..."
			ctx.JSON(200, errrsp)
		}

	} else if method == "" {
		suRsp.Msg = "更新中..."
		ctx.JSON(200, suRsp)
		versionInfo = common.GetRelease()
		path, _ := os.Executable()
		common.DownloadFileProgress(versionInfo.DownloadUrl, path+".tmp")
		os.Rename(path+".tmp", path)
		updateFlag = true
	}
}
