package weblib

import (
	"WorkReport/common"
	"fmt"
	"log"
	"net/http"
	"os"
	"sync"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/gorilla/websocket"
	"golang.org/x/crypto/bcrypt"
)

var (
	versionInfo = common.ReleaseVersion{}
)

var IsUpdated bool

func PasswordHash(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	return string(bytes), err
}

func PasswordVerify(password, hash string) bool {
	err := bcrypt.CompareHashAndPassword([]byte(hash), []byte(password))
	return err == nil
}

func UpdateCheck(ctx *gin.Context) {
	if IsUpdated {
		ctx.JSON(200, NewSuccessResponse("更新完成，请重启软件！", versionInfo))
		return
	}
	go func() {
		versionInfo = common.GetRelease()
	}()
	if versionInfo.TagName == "" {
		ctx.JSON(200, NewSuccessResponse("版本正在检测中...", versionInfo))
		return
	}
	if common.Version != versionInfo.TagName {
		var msg string
		if common.Version != "" {
			msg = fmt.Sprintf("有新版本可以更新!  当前版本%s,最新版本%s 点击更新", common.Version, versionInfo.TagName)
		} else {
			msg = fmt.Sprintf("有新版本可以更新!  最新版本%s 点击更新", versionInfo.TagName)
		}

		ctx.JSON(200, NewSuccessResponse(msg, versionInfo))
		return
	}

	ctx.JSON(200, NewEmptyDataSuccessResponse(fmt.Sprintf("已是最新版本%s", common.Version)))
}

var upgrade = websocket.Upgrader{
	// 允许所有CORS跨域访问
	CheckOrigin: func(_ *http.Request) bool {
		return true
	},
}

func Update(ctx *gin.Context) {
	wg := sync.WaitGroup{}
	client, err := upgrade.Upgrade(ctx.Writer, ctx.Request, nil)
	if err != nil {
		log.Println(err)
		return
	}

	if err = client.WriteMessage(websocket.TextMessage, []byte("开始更新,请稍候...")); err != nil {
		log.Println(err)
	}

	path, _ := os.Executable()

	wg.Add(2)

	go func() {
		if common.DownloadBar.State().CurrentPercent != 1 {
			common.DownloadFileProgress(versionInfo.DownloadUrl, path+".tmp")
		}
		if err = client.WriteMessage(websocket.TextMessage, []byte("更新完成，请重启软件！")); err != nil {
			log.Println(err)
		}
		IsUpdated = true
		wg.Done()
	}()

	go func() {
		for {
			time.Sleep(time.Second * 1)
			if common.DownloadBar.State().CurrentPercent > 0 {
				if err = client.WriteMessage(websocket.TextMessage, []byte(fmt.Sprintf("更新中...已完成%.2f%%", common.DownloadBar.State().CurrentPercent*100))); err != nil {
					log.Println(err)
				}
			}
			if common.DownloadBar.State().CurrentPercent == 1 {
				if err = client.WriteMessage(websocket.TextMessage, []byte("更新完成，请重启软件！")); err != nil {
					log.Println(err)
				}
				if err := os.Rename(path+".tmp", path); err != nil {
					log.Println(err)
				}
				wg.Done()
				break
			}
		}
	}()
	wg.Wait()

	client.Close()
}
