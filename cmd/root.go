package cmd

import (
	"fmt"
	"WorkReport/web"
	"WorkReport/web/model/utils"
	"github.com/spf13/cobra"
	"log"

	//"github.com/spf13/viper"
	"os"
)

//var cfgFile string
var (
	Name ="WorkReport"

	Port string

	//version info
	Version   string
	commitID  string
	buildTime string
	goVersion string
	buildUser string
)

// rootCmd represents the base command when called without any subcommands
var rootCmd = &cobra.Command{
	Use:   Name,
	Short: fmt.Sprintf("%s 是用于记录工作日志的系统",Name),
	Long:  fmt.Sprintf("%s 是用于记录工作日志的系统",Name),

	Run: func(cmd *cobra.Command, args []string) {
		cmd.Help()
	},
}

func Execute() {
	if err := rootCmd.Execute(); err != nil {
		log.Println(err)
		os.Exit(1)
	}
}

func init() {
	//cobra.OnInitialize(initConfig)
	runCmd.Flags().StringVarP(&Port, "port", "P", "8080", "web server listen port.")
	rootCmd.AddCommand(versionCmd)
	rootCmd.AddCommand(initCmd)
	rootCmd.AddCommand(runCmd)
	rootCmd.AddCommand(updateCmd)
}

var versionCmd = &cobra.Command{
	Use:   "version",
	Short: "Print the version number of workLog",
	Run: func(cmd *cobra.Command, args []string) {
		fmt.Printf("Version:   %s\n", Version)
		fmt.Printf("CommitID:  %s\n", commitID)
		fmt.Printf("BuildTime: %s\n", buildTime)
		fmt.Printf("GoVersion: %s\n", goVersion)
		fmt.Printf("BuildUser: %s\n", buildUser)
	},
}


var runCmd = &cobra.Command{
	Use:   "run",
	Short: "run server.",
	Run: func(cmd *cobra.Command, args []string) {
		utils.InitDB(DBUser, DBPwd, DBHost, DBPort, DBName )
		web.StrartServer(Port)
	},
}
