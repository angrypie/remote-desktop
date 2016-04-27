package server

import (
	"github.com/gorilla/websocket"
	"io"
	"log"
)

type Action struct {
	Action string      `json:"action"`
	Data   interface{} `json:"data"`
}

func sendAction(action *Action, conn *websocket.Conn) {
	if err := conn.WriteJSON(action); err != nil {
		log.Println("sendAction[", action.Action, "] WriteJSON: ", err)
	}
}

//Add new entry to hostConnections map
func hostRegister(data *interface{}, host *websocket.Conn) *chan bool {
	name, _ := (*data).(string)
	wait := make(chan bool)
	hostConnectios[name] = &Host{name, host, &wait, false}
	return &wait
}

//Send info about avaliable hosts to client.
//
func getHosts(data *interface{}, client *websocket.Conn) {
	type hostInfo struct {
		Login  string `json:"login"`
		Active bool   `json:"active"`
	}
	hosts := make([]hostInfo, len(hostConnectios))
	i := 0
	for _, host := range hostConnectios {
		hosts[i] = hostInfo{host.Login, host.Active}
		i++
	}
	sendAction(&Action{"AVALIABLE_HOSTS", hosts}, client)
}

//Client connects to host. Client and host send messages to each other
//and server doesn't process these messages, just forwards them
func selectHost(data *interface{}, client *websocket.Conn) {
	name, _ := (*data).(string)
	host := hostConnectios[name]
	if host.Active {
		sendAction(&Action{"HOST_BUSY", ""}, client)
		return
	}
	host.Active = true
	sendAction(&Action{"SELECT_SUCCESS", host.Login}, client)

	client_close := make(chan bool)
	host_close := make(chan bool)
	//client to host relay
	go websocketCopy(client, host.Conn, client_close)
	//host to client relay
	go websocketCopy(host.Conn, client, host_close)

	select {
	case <-client_close:
		sendAction(&Action{"CLIENT_CLOSE", ""}, host.Conn)
	case <-host_close:
		sendAction(&Action{"HOST_CLOSE", ""}, client)
	}
}

func websocketCopy(from, to *websocket.Conn, close chan bool) {
	defer func() {
		close <- true
	}()
	for {
		ActionType, r, err := from.NextReader()
		if err != nil {
			return
		}
		w, err := to.NextWriter(ActionType)
		if err != nil {
			log.Panic(err)
		}
		if _, err := io.Copy(w, r); err != nil {
			log.Panic(err)
		}
		if err := w.Close(); err != nil {
			log.Panic(err)
		}
	}
}
