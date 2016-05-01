package rodeo

import (
	"flag"
	"log"
)

func RodeoServer() {
	port := flag.String("port", "9595", "Specify listening port")
	flag.Parse()
	log.Println("Server started on port: ", *port)
}
