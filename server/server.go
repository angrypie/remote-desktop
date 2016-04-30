package server

import (
	"log"
)

func Start(port string) {
	log.Println("Server started on port: ", port)
	wsServerStart(port)
	//initDb()
}
