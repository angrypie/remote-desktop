package server

import (
	"github.com/gorilla/websocket"
	"log"
	"net/http"
)

func wsServerStart() {
	//dev
	port := ":9595"
	log.Printf("Start")
	hostConnectios = make(map[string]*Host)

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
		log.Panicln("wsUpgrade: ", err)
	}
	defer ws.Close()

	//dev
	log.Println("New connction: ", ws.RemoteAddr())

	msg := new(Action)
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
			wait := hostRegister(&msg.Data, ws)
			<-*wait
			break
		case "GET_HOSTS":
			getHosts(&msg.Data, ws)
			break
		case "SELECT_HOST":
			selectHost(&msg.Data, ws)
			break
		}
	}
}
