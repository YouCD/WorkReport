package Weblib

import (
	"github.com/gin-gonic/gin"
	"github.com/gobuffalo/packr/v2"
	"net/http"
	"path"
	"strings"
)

func exists(fs *packr.Box, prefix string, filepath string) bool {
	if p := strings.TrimPrefix(filepath, prefix); len(p) < len(filepath) {
		name := path.Join("/", p)
		if fs.HasDir(name) {
			index := path.Join(name, "index.html")
			if !fs.Has(index) {
				return false
			}
		} else if !fs.Has(name) {
			return false
		}
		return true
	}
	return false
}


func StaticServe(urlPrefix string, fs *packr.Box) gin.HandlerFunc {
	fileserver := http.FileServer(fs)
	if urlPrefix != "" {
		fileserver = http.StripPrefix(urlPrefix, fileserver)
	}
	return func(c *gin.Context) {
		if exists(fs, urlPrefix, c.Request.URL.Path) {
			fileserver.ServeHTTP(c.Writer, c.Request)
			c.Abort()
		}
	}
}



func NewGinRouter() *gin.Engine {
	gin.SetMode(gin.ReleaseMode)
	ginRouter := gin.Default()
	box := packr.New("gemini", "../dist")
	ginRouter.Use(StaticServe("/", box))

	ginRouter.Use(CorsMiddleware())

	//用户登入
	ginRouter.Handle("POST", "/login", Login)


	workLog := ginRouter.Group("/w")
	{
		//添加workLog
		workLog.Handle("POST", "/workLog", addWorkLog)
		//获取workLog
		workLog.Handle("GET", "/workLog", getWorkLog)
		//修改WorkLog
		workLog.Handle("PUT", "/workLog", modifyWorkLog)
		//删除WorkLog
		workLog.Handle("DELETE", "/workLog", delWorkLog)

		//获取工作大类
		workLog.Handle("GET", "/workType1", getWorkType1)
		//添加工作种类
		workLog.Handle("POST", "/workType", addWorkType)
		//获取工作子类
		workLog.Handle("GET", "/workType2", getWorkType2)
		//获取指定工作类别
		workLog.Handle("GET", "/workType", getWorkType)
		//修改指定工作类别
		workLog.Handle("PUT", "/workType", editWorkType)
		//通过type ID 获取设备工作内容
		workLog.Handle("GET", "/workLogFromType", getWorkLogFromType)
		//获取本周日志
		workLog.Handle("GET", "/workLogFromWeek", getWorkLogFromWeek)
		//获取本月日志
		workLog.Handle("GET", "/workLogFromMonth", getWorkLogFromWeek)
		//通过内容搜索日志
		workLog.Handle("GET", "/workLogFromContent", getWorkLogFromContent)
		//通过日期搜索日志
		workLog.Handle("GET", "/workLogFromDate", getWorkLogFromDate)
		//通过type1获得总数量
		workLog.Handle("GET", "/type1Count", gettype1Count)
		//通过type2获得总数量
		workLog.Handle("GET", "/type2Count", gettype2Count)

	}

	return ginRouter
}
