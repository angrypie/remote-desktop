package server

import (
	"github.com/gorilla/websocket"
	"log"
	"testing"
	"time"
)

//func TestStart(t *testing.T) {
//go Start()
//time.Sleep(time.Millisecond * 1)
//}

func TestWesocketServer(t *testing.T) {
	go wsServerStart()
	time.Sleep(time.Millisecond * 100)
	go addHost("HostFromGoTest#1")
	go addHost("HostFromGoTest#2")
	exit := make(chan bool)
	<-exit
}

func addHost(login string) {
	c, _, err := websocket.DefaultDialer.Dial("ws://localhost:9595", nil)
	if err != nil {
		log.Fatal("dial:", err)
	}
	defer c.Close()

	action := Message{"HOST_REGISTER", login}

	err = c.WriteJSON(&action)
	if err != nil {
		log.Println("write:", err)
		return
	}

	for {
		err = c.ReadJSON(&action)
		if err != nil {
			log.Println("readJson:", err)
			return
		}
		log.Println(login, ":\n", action.Action, "\n", action.Data)
	}
}
