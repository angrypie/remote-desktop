package main

import (
	"flag"
	"github.com/angrypie/remote-desktop/server"
)

var port = flag.String("port", "9595", "Specify listening port")

func main() {
	server.Start(*port)
}
