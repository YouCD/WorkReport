package cmd

import (
	"WorkReport/web/model/utils"

	"github.com/spf13/cobra"
	"github.com/youcd/toolkit/db"
	"github.com/youcd/toolkit/log"
	"gorm.io/gorm/logger"
)

func init() {
	resetCmd.Flags().StringVarP(&DBName, "DBName", "d", "worklog", "database name.")
	resetCmd.Flags().StringVarP(&DBPwd, "DBPwd", "p", "P@ssw0rd", "database password.")
	resetCmd.Flags().StringVarP(&DBUser, "DBUser", "u", "root", "database user.")
	resetCmd.Flags().StringVarP(&DBHost, "DBHost", "i", "127.0.0.1", "database host IP.")
	resetCmd.Flags().StringVarP(&DBPort, "DBPort", "c", "3306", "database host port.")
	resetCmd.Flags().StringVarP(&username, "username", "n", "admin", "login username.")
	resetCmd.Flags().StringVarP(&password, "password", "w", "P@ssw0rd", "login user password.")
}

var resetCmd = &cobra.Command{
	Use:   "reset",
	Short: "reset the login user password",
	Run: func(_ *cobra.Command, _ []string) {
		if DBUser != "" && DBPwd != "" && DBHost != "" && DBPort != "" && DBName != "" {
			db.InitDB(DBUser, DBPwd, DBHost, DBPort, DBName, logger.Silent)
			if err := utils.CreateOrUpdateUser(username, password); err != nil {
				log.Error(err)
			}
		}
	},
}
