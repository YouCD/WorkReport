package web

import (
	"WorkReport/web/Weblib"
)

func StrartServer( port string)  {
	router := Weblib.NewGinRouter()
	if port!=""{
		router.Run(":"+port)
	}
	router.Run()
}