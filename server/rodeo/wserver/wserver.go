package wserver

import (
	"github.com/gorilla/websocket"
	"io"
	"log"
	"net/http"
)

type Message struct {
	Reader *io.Reader
	Type   int
}

type WsServer struct {
	OnMessage func(msg *Message, conn *websocket.Conn)
}

var wsServer WsServer

// Run websocket-server on specified port.
func websocketServer(port string) {
	addr := ":" + port

	http.HandleFunc("/", connectionHandler)
	if err := http.ListenAndServe(addr, nil); err != nil {
		log.Panicln("ListenAndServer: ", err)
	}
}

//Set upgrader function, which takes http.ResponseWriter and http.Request
//and returns websocket.Conn (websocket conneciton). It's used in connection handler.
var gorillaUpgrader = websocket.Upgrader{
	ReadBufferSize:  2048,
	WriteBufferSize: 2048,
	CheckOrigin:     func(r *http.Request) bool { return true },
}

//Callback function that is called for new connection.
func connectionHandler(w http.ResponseWriter, r *http.Request) {
	conn, err := gorillaUpgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println("connectionHandler: gorillaUpgrader: ", err)
		return
	}
	defer conn.Close()

	//dev
	log.Println("New connection: ", conn.RemoteAddr())

	for {
		messageType, r, err := conn.NextReader()
		if err != nil {
			return
		}

		msg := &Message{&r, messageType}
		wsServer.OnMessage(msg, conn)
	}

}
