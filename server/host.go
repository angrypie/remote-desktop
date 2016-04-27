package server

import (
	"github.com/gorilla/websocket"
)

type Host struct {
	Login  string
	Conn   *websocket.Conn
	Wait   *chan bool
	Active bool
}

var hostConnectios map[string]*Host
