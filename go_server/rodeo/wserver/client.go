package wserver

import (
	"github.com/gorilla/websocket"
	"log"
)

type Client struct {
	onmessage OnMessageFunc   //handler for new messages from Conn
	Conn      *websocket.Conn //wesocket connection which server in connectionHandler
}

//Set new message handler for Client
func (c *Client) SetOnmessage(onmessage OnMessageFunc) {
	c.onmessage = onmessage
}

//Get message handler function for Client
func (c Client) Onmessage() OnMessageFunc {
	return c.onmessage
}

func (c *Client) SendJson(v interface{}) {
	if err := c.Conn.WriteJSON(v); err != nil {
		log.Println("sendJson[", v, "] WriteJSON: ", err)
	}
}
