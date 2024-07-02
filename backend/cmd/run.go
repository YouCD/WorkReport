package cmd

import (
	"WorkReport/common"
	"WorkReport/web"
	"fmt"
	"github.com/spf13/cobra"
	"github.com/youcd/toolkit/db"
	"gorm.io/gorm/logger"
	"log"
)

func init() {
	runCmd.Flags().StringVarP(&Port, "port", "P", "8080", "web server listen port.")
	runCmd.Flags().StringVarP(&DBName, "DBName", "d", "worklog", "database name.")
	runCmd.Flags().StringVarP(&DBPwd, "DBPwd", "p", "P@ssw0rd", "database password.")
	runCmd.Flags().StringVarP(&DBUser, "DBUser", "u", "root", "database user.")
	runCmd.Flags().StringVarP(&DBHost, "DBHost", "i", "127.0.0.1", "database host IP.")
}

var runCmd = &cobra.Command{
	Use:   "run",
	Short: "run server.",
	Run: func(_ *cobra.Command, _ []string) {
		if DBUser != "" && DBPwd != "" && DBHost != "" && DBPort != "" && DBName != "" {
			//utils.InitDB(DBUser, DBPwd, DBHost, DBPort, DBName)
			db.InitDB(DBUser, DBPwd, DBHost, DBPort, DBName, logger.Silent)
			fmt.Printf("\r  \033[36%s\033[m  ", Logo)
			//nolint:
			err := common.OpenBrowser(fmt.Sprintf("http://%s:%s/#/", "127.0.0.1", Port))
			if err != nil {
				log.Println(err)
			}
			web.StartServer(Port)
		}

	},
}
