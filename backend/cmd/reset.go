package cmd

import (
	"WorkReport/internal/config"
	"WorkReport/web/model/utils"

	"github.com/spf13/cobra"
	"github.com/youcd/toolkit/db"
	"github.com/youcd/toolkit/log"
	"gorm.io/gorm/logger"
)

func init() {
	resetCmd.Flags().StringVarP(&username, "username", "n", "admin", "login username.")
	resetCmd.Flags().StringVarP(&password, "password", "w", "P@ssw0rd", "login user password.")
}

var resetCmd = &cobra.Command{
	Use:   "reset",
	Short: "reset the login user password",
	Run: func(_ *cobra.Command, _ []string) {
		db.InitDB(config.Cfg.DB.User, config.Cfg.DB.Pwd, config.Cfg.DB.Host, config.Cfg.DB.Port, config.Cfg.DB.Name, logger.Silent)
		if err := utils.CreateOrUpdateUser(username, password); err != nil {
			log.Error(err)
		}
	},
}
