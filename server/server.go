package server

import (
	"flag"
	"log"
)

func Start() {
	var port = flag.String("port", "9595", "Specify listening port")
	log.Println("Server started on port: ", port)
	wsServerStart(*port)
	//initDb()
}
