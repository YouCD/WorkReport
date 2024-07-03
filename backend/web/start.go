package web

import (
	"WorkReport/web/weblib"
	"fmt"

	"github.com/youcd/toolkit/log"
)

func StartServer(port string) {
	fmt.Printf("\nweb server listen on %s\n", port)
	router := weblib.NewGinRouter()
	if port != "" {
		if err := router.Run(":" + port); err != nil {
			log.Error(err)
			return
		}
	}
	if err := router.Run(); err != nil {
		log.Error(err)
	}
}
