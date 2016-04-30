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

type hostInfo struct {
	Login  string `json:"login"`
	Active bool   `json:"active"`
}

func sendAction(action *Action, conn *websocket.Conn) {
	if err := conn.WriteJSON(action); err != nil {
		log.Println("sendAction[", action.Action, "] WriteJSON: ", err)
	}
}

//Add new entry to hostConnections map
func hostRegister(data *interface{}, host *websocket.Conn) *Host {
	name, _ := (*data).(string)
	wait := make(chan bool)
	new_host := &Host{name, host, &wait, false}
	hostConnections[name] = new_host
	return new_host
}

//Send info about avaliable hosts to client.
func getHosts(data *interface{}, client *websocket.Conn) {
	hosts := make([]hostInfo, len(hostConnections))
	i := 0
	for _, host := range hostConnections {
		hosts[i] = hostInfo{host.Login, host.Active}
		i++
	}
	sendAction(&Action{"AVALIABLE_HOSTS", hosts}, client)
}

//Client connects to host. Client and host send messages to each other
//and server doesn't process these messages, just forwards them
func selectHost(data *interface{}, client *websocket.Conn) {
	name, _ := (*data).(string)
	host := hostConnections[name]
	if host.Active {
		sendAction(&Action{"HOST_BUSY", ""}, client)
		return
	}
	host.Active = true

	sendAction(&Action{"CLIENT_CONNECT", ""}, host.Conn)

	//Waiting host response for client connection. If host has allowed connection(CLIENT_ACCESS)
	//than server sends SELECT_SUCCESS to client and connects client to host.
	//If host has denied connection(CLIENT_DENIED) than server sends SELECT_DENIED
	//and goes back to websocket handler.
	<-*host.Wait
	if host.Active == false {
		sendAction(&Action{"SELECT_DENIED", ""}, client)
		return
	}
	sendAction(&Action{"SELECT_SUCCESS", ""}, client)

	client_close := make(chan bool)
	host_close := make(chan bool)

	//client to host relay
	go websocketCopy(client, host.Conn, client_close)
	//host to client relay
	go websocketCopy(host.Conn, client, host_close)

	select {
	case <-client_close:
		sendAction(&Action{"CLIENT_CLOSE", ""}, host.Conn)
		host.Active = false
	case <-host_close:
		sendAction(&Action{"HOST_CLOSE", ""}, client)
		*host.Wait <- false
	}
}

func clientAccess(data *interface{}, host *Host) {
	host.Active = true
	*host.Wait <- false
	<-*host.Wait
}

func clientDenied(data *interface{}, host *Host) {
	host.Active = false
	*host.Wait <- false
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
