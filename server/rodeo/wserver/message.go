package wserver

import (
	"encoding/json"
	//"github.com/gorilla/websocket"
	"io"
	//"log"
	//"net/http"
)

//Readr and message type from websocket.NextReader method
type Message struct {
	Reader *io.Reader
	Type   int
}

//Type for callback functions.
type OnMessageFunc func(msg *Message, client *Client)
type OnCloseFunc func(client *Client)

//Decode message to json
func (msg Message) ToJson(v interface{}) error {
	err := json.NewDecoder(*msg.Reader).Decode(v)
	if err == io.EOF {
		err = io.ErrUnexpectedEOF
	}
	return err
}
