package web

import (
	"WorkReport/web/weblib"
	"fmt"
)

func StartServer(port string) {
	fmt.Printf("\nweb server listen on %s\n", port)
	router := weblib.NewGinRouter()
	if port != "" {
		if err := router.Run(":" + port); err != nil {
			fmt.Println(err)
			return
		}
	}
	if err := router.Run(); err != nil {
		fmt.Println(err)
	}
}
