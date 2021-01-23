package cmd

import (
	"WorkReport/common"
	"fmt"
	"github.com/spf13/cobra"
	"io"
	"log"
	"os"
)

var (
	path  string
	Force bool

)



func init() {
	updateCmd.Flags().BoolVarP(&Force, "force", "f", false, "force updating.")
}


var updateCmd = &cobra.Command{
	Use:   "update",
	Short: "update the WorkReport server",
	PersistentPostRun: func(cmd *cobra.Command, args []string) {
		os.Rename(path+".tmp", path)
	},
	Run: func(cmd *cobra.Command, args []string) {

		v := common.GetRelease()
		if Force {
			path, _ = os.Executable()
			common.DownloadFileProgress(v.DownloadUrl, path+".tmp")
			return
		} else if common.Version != v.TagName {
			path, _ = os.Executable()
			common.DownloadFileProgress(v.DownloadUrl, path+".tmp")

		} else {
			log.Println(fmt.Sprintf("version: %s. The version is latest version.", common.Version))
			return
		}

	},
}

type Reader struct {
	io.Reader
	Total   int64
	Current int64
}

