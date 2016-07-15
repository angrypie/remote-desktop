package rodeo

import (
	"github.com/angrypie/remote-desktop/go_server/rodeo/wserver"
	"io"
	"log"
)

type Relay struct {
	connected map[*wserver.Client]*Host
}

func NewRelay() *Relay {
	relay := Relay{make(map[*wserver.Client]*Host)}
	return &relay
}

func (r *Relay) GetHost(conn *wserver.Client) (host *Host, ok bool) {
	host, ok = r.connected[conn]
	return host, ok
}

func (r *Relay) GetClient(conn *wserver.Client) (host *wserver.Client, ok bool) {
	for client, host := range r.connected {
		if conn == host.Conn {
			return client, true
		}

	}
	return nil, false
}

func (r *Relay) Add(conn *wserver.Client, host *Host) {
	r.connected[conn] = host
}

func (r *Relay) Delete(conn *wserver.Client) {
	delete(r.connected, conn)
}

func copyMessage(to *wserver.Client) wserver.OnMessageFunc {
	handler := func(msg *wserver.Message, client *wserver.Client) {
		ActionType, r := msg.Type, msg.Reader

		w, err := to.Conn.NextWriter(ActionType)
		if err != nil {
			log.Println("copyMessage: NextWriter:", err)
			return
		}
		if _, err := io.Copy(w, *r); err != nil {
			log.Println("copyMessage: Copy:", err)
			return
		}
		if err := w.Close(); err != nil {
			log.Println("copyMessage: Close():", err)
			return
		}
	}

	return handler
}
