package common

import (
	"fmt"
	"github.com/schollz/progressbar/v3"
	"github.com/tidwall/gjson"
	"io"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"os/exec"
	"runtime"
	"strings"
)

const (
	GitHubReleaseUrl = "https://api.github.com/repos/youcd/WorkReport/releases/latest"
)

var commands = map[string]string{
	"windows": "cmd /c start",
	"darwin":  "open",
	"linux":   "xdg-open",
}

type ReleaseVersion struct {
	TagName     string `json:"tag_name"`
	SwName      string `json:"sw_name"`
	DownloadUrl string `json:"download_url"`
}

func GetRelease() (v ReleaseVersion) {
	//系统类型
	OS := runtime.GOOS
	resp, err := http.Get(GitHubReleaseUrl)
	if err != nil {
		log.Println(err)
	}
	defer resp.Body.Close()
	bytes, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Println(err)
	}
	vList := make([]ReleaseVersion, 0)
	count := gjson.Get(string(bytes), "assets.#").Int()

	for i := 0; i < int(count); i++ {
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

func DownloadFileProgress(url, filename string) {
	r, err := http.Get(url)
	if err != nil {
		panic(err)
	}
	defer func() { _ = r.Body.Close() }()
	f, err := os.Create(filename)
	// 更改权限
	err = f.Chmod(0775)
	if err != nil {
		panic(err)
	}
	defer func() { _ = f.Close() }()
	bar := progressbar.DefaultBytes(
		r.ContentLength,
		"下载中",
	)
	io.Copy(io.MultiWriter(f, bar), r.Body)
}

// Open calls the OS default program for uri
func OpenBrowser(uri string) error {
	run, ok := commands[runtime.GOOS]
	if !ok {
		return fmt.Errorf("don't know how to open things on %s platform", runtime.GOOS)
	}

	cmd := exec.Command(run, uri)
	return cmd.Start()
}
