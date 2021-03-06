package Weblib

import (
	"WorkReport/web/dist"
	"embed"
	"github.com/gin-gonic/gin"
	"net/http"
	"strings"
)

func StaticServe(urlPrefix string, fs embed.FS, ) gin.HandlerFunc {
	fileServer := http.FileServer(http.FS(fs))
	return func(ctx *gin.Context) {
		//fullName := filepath.Join("/dist", filepath.FromSlash(path.Clean("/"+ctx.Request.URL.Path)))
		//fmt.Println("fullName:",fullName)
		if strings.Contains(ctx.Request.URL.Path, "/js") || strings.Contains(ctx.Request.URL.Path, "/css") || ctx.Request.URL.Path == "/" {

			fileServer.ServeHTTP(ctx.Writer, ctx.Request)
			ctx.Abort()
			return
		}
	}

}

func NewGinRouter() *gin.Engine {

	ginRouter := gin.Default()
	ginRouter.Use(StaticServe("/", dist.Dist))

	ginRouter.Use(CorsMiddleware())

	//用户登入
	ginRouter.Handle("POST", "/login", Login)

	//ginRouter.Use(JWTAuthMiddleware())
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
		//下载指定时间范围的工作日志
		workLog.Handle("GET", "/downloadWorklog", downloadWorklog)


	}
	ginRouter.Handle("GET", "/updateCheck", UpdateCheck)
	ginRouter.Handle("GET", "/update", Update)

	return ginRouter
}
