package main

import (
	"WorkReport/cmd"
)
//打包静态文件命令，生成 packr.go 文件
//go:generate packr2
func main() {
	cmd.Execute()
}