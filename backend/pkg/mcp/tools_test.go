package mcp

import (
	"WorkReport/internal/config"
	"context"
	"fmt"
	"testing"

	"github.com/mark3labs/mcp-go/mcp"
	"github.com/youcd/toolkit/db"
	"gorm.io/gorm/logger"
)

func init() {
	config.ParserConfig("/home/ycd/self_data/source_code/WorkReport/backend/internal/config/config.yaml")
	db.InitDB(config.Cfg.DB.User, config.Cfg.DB.Pwd, config.Cfg.DB.Host, config.Cfg.DB.Port, config.Cfg.DB.Name, logger.Silent)
}
func TestListWorkTypes(t *testing.T) {
	types, err := ListWorkTypes(context.Background(), mcp.CallToolRequest{})
	if err != nil {
		t.Fatal(err)
	}
	t.Log(types)
}

func TestAddWorkLog(t *testing.T) {
	_, _ = ListWorkTypes(context.Background(), mcp.CallToolRequest{})

	for k, v := range typesCache {
		fmt.Printf("%#v,%#v\n", k, v)
	}

	p := make(map[string]any)

	p["content"] = "南网柔性单机版磁盘升级方案编写已测试，以及前期升级准备工作"
	log, err := AddWorkLog(context.Background(), mcp.CallToolRequest{Params: mcp.CallToolParams{
		Name:      "addWorkLog",
		Arguments: p,
		Meta:      nil,
	}})
	if err != nil {
		t.Fatal(err)
	}
	t.Log(log)
}
