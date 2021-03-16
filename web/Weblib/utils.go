package Weblib

import (
	"WorkReport/common"
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/gorilla/websocket"
	"golang.org/x/crypto/bcrypt"
	"log"
	"net/http"
	"os"
	"sync"
	"time"
)

var (
	versionInfo = common.ReleaseVersion{}
)

func init() {
	versionInfo = common.GetRelease()
}

func PasswordHash(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	return string(bytes), err
}

func PasswordVerify(password, hash string) bool {
	err := bcrypt.CompareHashAndPassword([]byte(hash), []byte(password))
	return err == nil
}

func UpdateCheck(ctx *gin.Context) {
	if common.Version != versionInfo.TagName {
		suRsp.Data = versionInfo
		suRsp.Msg = fmt.Sprintf("有新版本可以更新!  当前版本%s,最新版本%s 点击更新", common.Version, versionInfo.TagName)
		ctx.JSON(200, suRsp)
	} else {
		errrsp.Msg = fmt.Sprintf("已是最新版本%s", common.Version)
		ctx.JSON(200, errrsp)
	}

}

var upgrade = websocket.Upgrader{
	// 允许所有CORS跨域访问
	CheckOrigin: func(r *http.Request) bool {
		return true
	},
}

func Update(ctx *gin.Context) {
	wg := sync.WaitGroup{}
	client, err := upgrade.Upgrade(ctx.Writer, ctx.Request, nil)
	if err != nil {
		log.Println(err)
	}

	err = client.WriteMessage(websocket.TextMessage, []byte("开始更新,请稍候..."))
	if err != nil {
		log.Println(err)
	}

	path, _ := os.Executable()

	wg.Add(2)

	go func() {
		if common.DownloadBar.State().CurrentPercent != 1 {
			common.DownloadFileProgress(versionInfo.DownloadUrl, path+".tmp")
		}
		err = client.WriteMessage(websocket.TextMessage, []byte("更新完成，请重启软件！"))
		wg.Done()
	}()

	go func() {
		for {
			time.Sleep(time.Second * 1)
			if common.DownloadBar.State().CurrentPercent > 0 {
				err = client.WriteMessage(websocket.TextMessage, []byte(fmt.Sprintf("更新中...已完成%.2f%%", common.DownloadBar.State().CurrentPercent*100)))
				if err != nil {
					log.Println(err)
				}
			}
			if common.DownloadBar.State().CurrentPercent == 1 {
				err = client.WriteMessage(websocket.TextMessage, []byte("更新完成，请重启软件！"))
				if err != nil {
					log.Println(err)
				}
				os.Rename(path+".tmp", path)
				wg.Done()
				break
			}
		}
	}()
	wg.Wait()

	client.Close()
}
