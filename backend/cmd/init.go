package cmd

import (
	"WorkReport/internal/config"
	"WorkReport/web/model/utils"

	"github.com/spf13/cobra"
	"github.com/youcd/toolkit/log"
)

var (
	username string
	password string
)

func init() {
	initCmd.Flags().StringVarP(&username, "username", "n", "admin", "login username.")
	initCmd.Flags().StringVarP(&password, "password", "w", "P@ssw0rd", "login user password.")
}

var initCmd = &cobra.Command{
	Use:   "init",
	Short: "init database.",
	Run: func(cmd *cobra.Command, _ []string) {
		if err := utils.InitTables(config.Cfg.DB.User, config.Cfg.DB.Pwd, config.Cfg.DB.Host, config.Cfg.DB.Port, config.Cfg.DB.Name, username, password); err != nil {
			log.Error(err)
			return
		}
	},
}
