package main

import (
	"WorkReport/cmd"

	"github.com/youcd/toolkit/log"
)

func main() {
	log.Init(true)
	cmd.Execute()
}
