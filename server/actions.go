package server

import (
	"github.com/gorilla/websocket"
	"io"
	"log"
)

//Add new entry to hostConnections map
func hostRegister(data *string, host *websocket.Conn) *chan bool {
	wait := make(chan bool)
	hostConnectios[*data] = &Host{*data, host, &wait, false}
	return &wait
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
	if host.active {
		host_busy := Message{"HOST_BUSY", ""}
		if err := client.WriteJSON(&host_busy); err != nil {
			log.Println("host_busy, WriteJSON: ", err)
		}
		return
	}
	host.active = true

	select_success := Message{"SELECT_SUCCESS", host.login}
	if err := client.WriteJSON(&select_success); err != nil {
		log.Println("select_sucess, WriteJSON: ", err)
	}

	exit := make(chan bool)
	//client to host
	go func() {
		defer func() {
			exit <- true
		}()
		for {
			messageType, r, err := client.NextReader()
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
	}()

	//host to client
	go func() {
		defer func() {
			exit <- true
		}()
		for {
			messageType, r, err := host.conn.NextReader()
			if err != nil {
				return
			}
			w, err := client.NextWriter(messageType)
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
	}()
	<-exit
	host.active = false
	client_close := Message{"CLIENT_CLOSE", ""}
	if err := host.conn.WriteJSON(&client_close); err != nil {
		log.Println("client_close, WriteJSON: ", err)
	}
}
