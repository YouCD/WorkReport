package weblib

import (
	"WorkReport/internal/config"
	"WorkReport/pkg/mcp"
	"WorkReport/web/dist"
	"embed"
	"net/http"
	"strings"

	"github.com/gin-gonic/gin"
)

func StaticServe(fs embed.FS) gin.HandlerFunc {
	fileServer := http.FileServer(http.FS(fs))
	return func(ctx *gin.Context) {
		// fullName := filepath.Join("/dist", filepath.FromSlash(path.Clean("/"+ctx.Request.URL.Path)))
		// fmt.Println("fullName:",fullName)
		if strings.Contains(ctx.Request.URL.Path, "/assets") || ctx.Request.URL.Path == "/" {
			fileServer.ServeHTTP(ctx.Writer, ctx.Request)
			ctx.Abort()
			return
		}
	}
}

func NewGinRouter() *gin.Engine {
	ginRouter := gin.Default()
	ginRouter.Use(StaticServe(dist.Dist))

	ginRouter.Use(CorsMiddleware())

	mcp.NewMCPServer(config.Cfg.Global.Token).RunWithGin(ginRouter)

	// 用户登入
	ginRouter.Handle(http.MethodPost, "/api/login", Login)

	// ginRouter.Use(JWTAuthMiddleware())
	workLog := ginRouter.Group("/api/w")
	workLog.Use(JWTAuthMiddleware())
	{
		// 添加workLog
		workLog.Handle(http.MethodPost, "/workLog", addWorkLog)
		// 获取workLog
		workLog.Handle(http.MethodGet, "/workLog", getWorkLog)
		// 修改WorkLog
		workLog.Handle(http.MethodPut, "/workLog", modifyWorkLog)
		// 删除WorkLog
		workLog.Handle(http.MethodDelete, "/workLog", delWorkLog)
		// 获取工作大类
		workLog.Handle(http.MethodGet, "/workType1", getWorkType1)
		// 添加工作种类
		workLog.Handle(http.MethodPost, "/workType", addWorkType)
		// 获取工作子类
		workLog.Handle(http.MethodGet, "/workType2", getWorkType2)
		// 获取指定工作类别
		workLog.Handle(http.MethodGet, "/workType", getWorkType)
		// 修改指定工作类别
		workLog.Handle(http.MethodPut, "/workType", editWorkType)
		// 通过type ID 获取设备工作内容
		workLog.Handle(http.MethodGet, "/workLogFromType", getWorkLogFromType)
		// 获取本周日志
		workLog.Handle(http.MethodGet, "/workLogFromWeek", getWorkLogFromWeek)
		// 获取本月日志
		workLog.Handle(http.MethodGet, "/workLogFromMonth", getWorkLogFromWeek)
		// 通过内容搜索日志
		workLog.Handle(http.MethodGet, "/workLogFromContent", getWorkLogFromContent)
		// 通过日期搜索日志
		workLog.Handle(http.MethodGet, "/workLogFromDate", getWorkLogFromDate)
		// 通过type1获得总数量
		workLog.Handle(http.MethodGet, "/type1Count", gettype1Count)
		// 通过type2获得总数量
		workLog.Handle(http.MethodGet, "/type2Count", gettype2Count)
		// 下载指定时间范围的工作日志
		workLog.Handle(http.MethodGet, "/downloadWorklog", downloadWorklog)
		// 更新
		workLog.Handle(http.MethodGet, "/updateCheck", UpdateCheck)
		workLog.Handle(http.MethodGet, "/update", Update)
	}
	ai := ginRouter.Group("/api/ai")
	ai.Use(JWTAuthMiddleware())
	{
		ai.Handle(http.MethodPost, "/addContent", addContent)
		ai.Handle(http.MethodGet, "/workLogFromWeek", workLogFromWeek)
		ai.Handle(http.MethodPost, "/sendEmail", sendEmail)
	}

	return ginRouter
}
