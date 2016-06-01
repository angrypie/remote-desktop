package main

import (
	"github.com/angrypie/remote-desktop/server/rodeo"
	"log"
)

func main() {
	log.Println("Test Rodeo Server")
	rodeo.RodeoServer()
	exit := make(chan bool)
	<-exit
}
