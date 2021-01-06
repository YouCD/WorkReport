package cmd

import (
	"fmt"
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
	rootCmd.AddCommand(versionCmd)
	rootCmd.AddCommand(initCmd)
	rootCmd.AddCommand(runCmd)
	rootCmd.AddCommand(updateCmd)
	rootCmd.AddCommand(resetCmd)
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


