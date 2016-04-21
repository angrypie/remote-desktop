package server

import (
	"github.com/gorilla/websocket"
)

type Host struct {
	login string
	conn  *websocket.Conn
}

var hostConnectios map[string]*Host
