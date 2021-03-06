package web

import (
	"WorkReport/web/Weblib"
	"fmt"
)

func StartServer(port string) {
	fmt.Printf("\nweb server listen on %s\n", port)
	router := Weblib.NewGinRouter()
	if port != "" {
		router.Run(":" + port)
	}
	router.Run()
}
