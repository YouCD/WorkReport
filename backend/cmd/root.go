package cmd

import (
	"WorkReport/common"
	"WorkReport/internal/config"
	"fmt"
	"os"

	"github.com/spf13/cobra"
	"github.com/youcd/toolkit/db"
	"github.com/youcd/toolkit/file"
	"github.com/youcd/toolkit/log"
	"gorm.io/gorm/logger"
)

// var cfgFile string
var (
	Name       = "WorkReport"
	configFile string
)

//nolint:dupword
const Logo = `                                                                      
m     m               #      mmmmm                                m   
#  #  #  mmm    m mm  #   m  #   "#  mmm   mmmm    mmm    m mm  mm#mm 
" #"# # #" "#   #"  " # m"   #mmmm" #"  #  #" "#  #" "#   #"  "   #   
 ## ##" #   #   #     #"#    #   "m #""""  #   #  #   #   #       #   
 #   #  "#m#"   #     #  "m  #    " "#mm"  ##m#"  "#m#"   #       "mm 
                                           #                          
                                           "                          
`

// rootCmd represents the base command when called without any subcommands
var rootCmd = &cobra.Command{
	Use:  Name,
	Long: fmt.Sprintf("%s 是用于记录工作日志的系统", Name),
	PersistentPreRun: func(cmd *cobra.Command, args []string) {
		if !file.Exists(configFile) {
			fmt.Println("请指定配置文件： -f ")
			os.Exit(1)
		}
		config.ParserConfig(configFile)
		var logLevel logger.LogLevel
		if config.Cfg.Global.LogLevel == "debug" {
			logLevel = logger.Info
		} else {
			logLevel = logger.Silent
		}
		db.InitDB(config.Cfg.DB.User, config.Cfg.DB.Pwd, config.Cfg.DB.Host, config.Cfg.DB.Port, config.Cfg.DB.Name, logLevel)
		log.Init(nil)
		log.SetLogLevel(config.Cfg.Global.LogLevel)
	},
	Run: func(cmd *cobra.Command, _ []string) {
		_ = cmd.Help()
	},
}

func Execute() {
	if err := rootCmd.Execute(); err != nil {
		log.Error(err)
		os.Exit(1)
	}
}

func init() {
	// cobra.OnInitialize(initConfig)
	rootCmd.AddCommand(versionCmd,
		initCmd,
		runCmd,
		updateCmd,
		resetCmd,
	)

	rootCmd.PersistentFlags().StringVarP(&configFile, "config", "f", "", "配置文件")
}

var versionCmd = &cobra.Command{
	Use:   "version",
	Short: "Print the version number of workLog",
	Run: func(_ *cobra.Command, _ []string) {
		fmt.Printf("Version:   %s\n", common.Version)
		fmt.Printf("CommitID:  %s\n", common.CommitID)
		fmt.Printf("BuildTime: %s\n", common.BuildTime)
		fmt.Printf("GoVersion: %s\n", common.GoVersion)
		fmt.Printf("BuildUser: %s\n", common.BuildUser)
	},
}
