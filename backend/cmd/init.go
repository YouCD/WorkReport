package cmd

import (
	"WorkReport/web/model/utils"

	"github.com/spf13/cobra"
	"github.com/youcd/toolkit/log"
)

var (
	DBUser   string
	DBPwd    string
	DBHost   string
	DBPort   string
	DBName   string
	username string
	password string
)

func init() {
	initCmd.Flags().StringVarP(&DBName, "DBName", "d", "worklog", "database name.")
	initCmd.Flags().StringVarP(&DBPwd, "DBPwd", "p", "P@ssw0rd", "database password.")
	initCmd.Flags().StringVarP(&DBUser, "DBUser", "u", "root", "database user.")
	initCmd.Flags().StringVarP(&DBHost, "DBHost", "i", "127.0.0.1", "database host IP.")
	initCmd.Flags().StringVarP(&DBPort, "DBPort", "c", "3306", "database host port.")
	initCmd.Flags().StringVarP(&username, "username", "n", "admin", "login username.")
	initCmd.Flags().StringVarP(&password, "password", "w", "P@ssw0rd", "login user password.")
}

var initCmd = &cobra.Command{
	Use:   "init",
	Short: "init database.",
	Run: func(cmd *cobra.Command, _ []string) {
		if DBUser != "" && DBPwd != "" && DBHost != "" && DBPort != "" && DBName != "" && username != "" && password != "" {
			if err := utils.InitTables(DBUser, DBPwd, DBHost, DBPort, DBName, username, password); err != nil {
				log.Error(err)
				return
			}
		} else {
			log.Info("please enter DBUser,DBPwd,DBHost,DBPort,DBName.")
			_ = cmd.Help()
		}

	},
}
