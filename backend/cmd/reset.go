package cmd

import (
	"WorkReport/internal/config"
	"WorkReport/web/model/utils"
	"time"

	"github.com/spf13/cobra"
	"github.com/youcd/toolkit/db"
	"github.com/youcd/toolkit/log"
)

func init() {
	resetCmd.Flags().StringVarP(&username, "username", "n", "admin", "login username.")
	resetCmd.Flags().StringVarP(&password, "password", "w", "P@ssw0rd", "login user password.")
}

var resetCmd = &cobra.Command{
	Use:   "reset",
	Short: "reset the login user password",
	Run: func(cmc *cobra.Command, _ []string) {
		ctx := cmc.Context()
		db.InitDB(config.Cfg.DB.User, config.Cfg.DB.Pwd, config.Cfg.DB.Host, config.Cfg.DB.Port, config.Cfg.DB.Name, log.NewGormLogger(time.Second*1, config.Cfg.Global.LogLevel))
		if err := utils.CreateOrUpdateUser(ctx, username, password); err != nil {
			log.WithCtx(ctx).Error(err)
		}
	},
}
