package mcp

import (
	"context"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/mark3labs/mcp-go/mcp"
	"github.com/mark3labs/mcp-go/server"
	"github.com/youcd/toolkit/log"
)

type MCPServer struct {
	*server.MCPServer
	token string
}

func NewMCPServer(token string) *MCPServer {
	s := server.NewMCPServer(
		"YCD-MCP",
		"1.0.0",
		server.WithLogging(),
		server.WithInstructions("工作日志记录助手，请输入指令进行操作。"),
	)

	return &MCPServer{s, token}
}

func (s *MCPServer) RunWithGin(router *gin.Engine) {
	stream := server.NewStreamableHTTPServer(s.MCPServer, server.WithLogger(log.GetLogger()))
	s.Run(context.Background())
	// 将MCP处理程序注册到Gin路由器
	router.Any("/mcp", func(c *gin.Context) {
		// 应用token验证
		token := c.Query("token")
		if token != s.token {
			c.AbortWithStatusJSON(http.StatusUnauthorized, gin.H{"error": "unauthorized"})
			return
		}

		// 转发请求给MCP处理器
		stream.ServeHTTP(c.Writer, c.Request)
	})
}

func (s *MCPServer) Run(_ context.Context) {
	s.RegisterTool(addWorkLog, AddWorkLog)
}
func (s *MCPServer) RegisterTool(tool mcp.Tool, handler server.ToolHandlerFunc) *MCPServer {
	s.AddTool(tool, handler)
	return s
}
