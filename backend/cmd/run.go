package cmd

import (
	"fmt"

	"WorkReport/common"
	"WorkReport/internal/config"
	"WorkReport/pkg/mcp"
	"WorkReport/web"

	m "github.com/mark3labs/mcp-go/mcp"
	"github.com/spf13/cobra"
	"github.com/youcd/toolkit/log"
)

var runCmd = &cobra.Command{
	Use:   "run",
	Short: "run server.",
	Run: func(cmd *cobra.Command, _ []string) {
		ctx := cmd.Context()
		fmt.Printf("\r  \033[36%s\033[m  ", Logo)
		//nolint:nosprintfhostport
		err := common.OpenBrowser(fmt.Sprintf("http://%s:%s/#/", "127.0.0.1", config.Cfg.Global.Port))
		if err != nil {
			log.WithCtx(ctx).Error(err)
		}
		_, _ = mcp.ListWorkTypes(ctx, m.CallToolRequest{})
		web.StartServer(config.Cfg.Global.Port)
	},
}
