package wserver

import (
	"github.com/gorilla/websocket"
	"log"
	"net/http"
)

type WServer struct {
	OnMessage OnMessageFunc
	OnClose   OnCloseFunc
}

// Run websocket-server on specified port.
func WebsocketServer(onmessage OnMessageFunc, onclose OnCloseFunc, port string) *WServer {
	addr := ":" + port
	server := &WServer{onmessage, onclose}
	go func() {
		http.HandleFunc("/", getConnectionHandler(server))
		if err := http.ListenAndServe(addr, nil); err != nil {
			log.Println("ListenAndServer: ", err)
		}
	}()
	return server
}

//Set upgrader function, which takes http.ResponseWriter and http.Request
//and returns websocket.Conn (websocket conneciton). It's used in connection handler.
var gorillaUpgrader = websocket.Upgrader{
	ReadBufferSize:  2048,
	WriteBufferSize: 2048,
	CheckOrigin:     func(r *http.Request) bool { return true },
}

//Returns http.HandlerFunc that has able to access to server from closure
func getConnectionHandler(server *WServer) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		connectionHandler(w, r, server)
	}
}

//Callback function that is called for new connection.
func connectionHandler(w http.ResponseWriter, r *http.Request, server *WServer) {
	conn, err := gorillaUpgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println("connectionHandler: gorillaUpgrader: ", err)
		return
	}
	defer func() {
		//dev
		log.Println("Connection close: ", conn.RemoteAddr())
		conn.Close()
	}()

	//dev
	log.Println("New connection: ", conn.RemoteAddr())

	client := Client{server.OnMessage, conn}
	defer server.OnClose(&client)
	for {
		messageType, r, err := conn.NextReader()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway) {
				log.Println("Connection handler: ", err)
			}
			return
		}
		new_msg := &Message{&r, messageType}
		client.onmessage(new_msg, &client)
	}
}
