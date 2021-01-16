package web

import (
	"WorkReport/web/Weblib"
	"fmt"
)

func StrartServer( port string)  {
	fmt.Printf("web server listen on %s", port)
	router := Weblib.NewGinRouter()
	if port!=""{
		router.Run(":"+port)
	}
	router.Run()
}