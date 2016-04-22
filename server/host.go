package server

import (
	"github.com/gorilla/websocket"
)

type Host struct {
	login  string
	conn   *websocket.Conn
	wait   *chan bool
	active bool
}

var hostConnectios map[string]*Host
