package common

import (
	"fmt"
	"io"
	"net/http"
	"os"
	"os/exec"
	"runtime"
	"strings"

	"github.com/schollz/progressbar/v3"
	"github.com/tidwall/gjson"
	"github.com/youcd/toolkit/log"
)

//nolint:revive
const (
	GitHubReleaseUrl = "https://api.github.com/repos/youcd/WorkReport/releases/latest"
)

var commands = map[string]string{
	"windows": "explorer.exe",
	"darwin":  "open",
	"linux":   "xdg-open",
}

//nolint:tagliatelle,revive
type ReleaseVersion struct {
	TagName     string `json:"tag_name"`
	SwName      string `json:"sw_name"`
	DownloadUrl string `json:"download_url"`
}

func GetRelease() ReleaseVersion {
	// 系统类型
	OS := runtime.GOOS
	//nolint:gosec
	resp, err := http.Get(GitHubReleaseUrl)
	if err != nil {
		log.Error(err)
	}
	defer resp.Body.Close()
	bytes, err := io.ReadAll(resp.Body)
	if err != nil {
		log.Error(err)
	}
	vList := make([]ReleaseVersion, 0)
	count := gjson.Get(string(bytes), "assets.#").Int()
	var v ReleaseVersion
	for i := range count {
		v.TagName = gjson.Get(string(bytes), "tag_name").Str
		v.SwName = gjson.Get(string(bytes), fmt.Sprintf("assets.%d.name", i)).Str
		v.DownloadUrl = gjson.Get(string(bytes), fmt.Sprintf("assets.%d.browser_download_url", i)).Str
		vList = append(vList, v)
	}
	switch OS {
	case "linux":
		for _, v := range vList {
			if strings.Contains(v.SwName, "linux") {
				return v
			}
		}
	case "darwin":
		for _, v := range vList {
			if strings.Contains(v.SwName, "darwin") {
				return v
			}
		}
	case "windows":
		for _, v := range vList {
			if strings.Contains(v.SwName, "windows") {
				return v
			}
		}
	}
	return v
}

var (
	DownloadBar = new(progressbar.ProgressBar)
)

func DownloadFileProgress(url, filename string) {
Download:
	//nolint:gosec
	r, err := http.Get(url)
	if err != nil {
		log.Error(err)
		goto Download
	}
	defer func() { _ = r.Body.Close() }()
	f, err := os.Create(filename)
	if err != nil {
		log.Error(err)
		return
	}
	// 更改权限
	err = f.Chmod(0775)
	if err != nil {
		log.Error(err)
		err = os.Rename(filename, strings.Split(filename, ".tmp")[0])
		log.Error(err)
	}
	defer func() {
		_ = f.Close()
		log.Info("更新退出程序.....")
	}()
	DownloadBar = progressbar.DefaultBytes(
		r.ContentLength,
		"下载中",
	)
	_, _ = io.Copy(io.MultiWriter(f, DownloadBar), r.Body)
}

// 运行时打开系统浏览器
func OpenBrowser(uri string) error {
	run, ok := commands[runtime.GOOS]
	if !ok {
		//nolint:err113
		return fmt.Errorf("don't know how to open things on %s platform", runtime.GOOS)
	}

	cmd := exec.Command(run, uri)
	//nolint:wrapcheck
	return cmd.Start()
}
