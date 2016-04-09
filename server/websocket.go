package server

import (
	"github.com/gorilla/websocket"
	"log"
	"net/http"
)

func wsServerStart() {
	log.Printf("Start")
	http.HandleFunc("/", wsServerHandler)

	if err := http.ListenAndServe(":6789", nil); err != nil {
		log.Panicln("ListenAndServe: ", err)
	}
}

var upgrader = websocket.Upgrader{
	ReadBufferSize:  1024,
	WriteBufferSize: 1024,
	CheckOrigin:     func(r *http.Request) bool { return true },
}

func wsServerHandler(w http.ResponseWriter, r *http.Request) {
	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Panicln("wsUpgrade: ", err)
	}

	defer ws.Close()
	//dev
	defer log.Println("Conn closed")
	//dev
	log.Println("New connction: ", ws.RemoteAddr())
	for {
		_, msg, err := ws.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway) {
				log.Println("wsHandler: ", err)
			}
			break
		}
		log.Println(msg)
	}
}
