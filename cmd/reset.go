package cmd

import (
	"WorkReport/web/model/utils"
	"github.com/spf13/cobra"
	"log"
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
	Run: func(cmd *cobra.Command, args []string) {
		if DBUser != "" && DBPwd != "" && DBHost != "" && DBPort != "" && DBName != "" {
			utils.InitDB(DBUser, DBPwd, DBHost, DBPort, DBName)
			err := utils.CreateOrUpdateUser(username, password)
			if err != nil {
				log.Println(err)
			}
		}
	},
}
