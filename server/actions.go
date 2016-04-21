package server

import (
	"github.com/gorilla/websocket"
	"io"
	"log"
)

//Add new entry to hostConnections map
func hostRegister(data *string, host *websocket.Conn) {
	hostConnectios[*data] = &Host{*data, host}
}

//Return info about avaliable hosts to client.
//Server send HOSTS with array of host names
func getHosts(data *string, client *websocket.Conn) {
	hosts := make([]string, len(hostConnectios))
	i := 0
	for host := range hostConnectios {
		hosts[i] = host
		i++
	}
	action := struct {
		Action string
		Data   []string
	}{
		"AVALIABLE_HOSTS",
		hosts,
	}

	if err := client.WriteJSON(action); err != nil {
		log.Println("getHosts, WriteJSON: ", err)
	}
}

func selectHost(data *string, client *websocket.Conn) {
	host := hostConnectios[*data]
	for {
		log.Println("wait")
		messageType, r, err := client.NextReader()
		log.Println("wait2")
		if err != nil {
			return
		}

		w, err := host.conn.NextWriter(messageType)
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
