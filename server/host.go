package server

import (
	"github.com/gorilla/websocket"
	"log"
)

type Host struct {
	Login  string
	Conn   *websocket.Conn
	Wait   *chan bool
	Active bool
}

var hostConnections map[string]*Host

func removeHostCennection(host *Host) {
	delete(hostConnections, host.Login)
}

func watchHostConnection(host *Host) {
	go func() {
		for {
			//action := new(Action)
			//err := host.Conn.ReadJSON(action)
			_, _, err := host.Conn.NextReader()
			log.Println("teee")
			if err != nil {
				if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway) {
					log.Println("Websocket handler: ", err)
				}
				host.Conn.Close()
				host.Conn = nil
				*host.Wait <- false
				break
			}
		}
	}()
	<-*host.Wait
}
