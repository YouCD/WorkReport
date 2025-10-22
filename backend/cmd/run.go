package cmd

import (
	"WorkReport/common"
	"WorkReport/internal/config"
	"WorkReport/pkg/mcp"
	"WorkReport/web"
	"context"
	"fmt"

	m "github.com/mark3labs/mcp-go/mcp"
	"github.com/spf13/cobra"
	"github.com/youcd/toolkit/log"
)

var runCmd = &cobra.Command{
	Use:   "run",
	Short: "run server.",
	Run: func(_ *cobra.Command, _ []string) {
		fmt.Printf("\r  \033[36%s\033[m  ", Logo)
		//nolint:nosprintfhostport
		err := common.OpenBrowser(fmt.Sprintf("http://%s:%s/#/", "127.0.0.1", config.Cfg.Global.Port))
		if err != nil {
			log.Error(err)
		}
		_, _ = mcp.ListWorkTypes(context.Background(), m.CallToolRequest{})
		web.StartServer(config.Cfg.Global.Port)

	},
}
