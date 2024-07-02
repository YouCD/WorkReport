package cmd

import (
	"WorkReport/common"
	"fmt"
	"github.com/spf13/cobra"
	"log"

	//"github.com/spf13/viper"
	"os"
)

// var cfgFile string
var (
	Name = "WorkReport"

	Port string
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
	Use:   Name,
	Short: fmt.Sprintf("%s 是用于记录工作日志的系统", Name),
	Long:  fmt.Sprintf("%s 是用于记录工作日志的系统", Name),

	Run: func(cmd *cobra.Command, _ []string) {
		_ = cmd.Help()
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
	rootCmd.AddCommand(versionCmd)
	rootCmd.AddCommand(initCmd)
	rootCmd.AddCommand(runCmd)
	rootCmd.AddCommand(updateCmd)
	rootCmd.AddCommand(resetCmd)
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
