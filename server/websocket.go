package server

import (
	"github.com/gorilla/websocket"
	"log"
	"net/http"
)

func wsServerStart(port string) {
	port = ":" + port
	//dev
	log.Printf("Start")
	hostConnections = make(map[string]*Host)

	http.HandleFunc("/", wsServerHandler)
	if err := http.ListenAndServe(port, nil); err != nil {
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
		log.Println("wsUpgrade: ", err)
	}
	defer ws.Close()

	//dev
	log.Println("New connction: ", ws.RemoteAddr())

	msg := new(Action)
	var host *Host = nil
	for {
		err := ws.ReadJSON(msg)
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway) {
				log.Println("Websocket handler: ", err)
			}
			break
		}

		switch msg.Action {
		case "HOST_REGISTER":
			host = hostRegister(&msg.Data, ws)
		case "GET_HOSTS":
			getHosts(&msg.Data, ws)
		case "SELECT_HOST":
			selectHost(&msg.Data, ws)
		case "CLIENT_ACCESS":
			clientAccess(&msg.Data, host)
		case "CLIENT_DENIED":
			clientDenied(&msg.Data, host)
		}
	}
}
