package rodeo

import (
	"flag"
	"github.com/angrypie/remote-desktop/server/rodeo/wserver"
	"io"
	"log"
)

var clientConnected map[*wserver.Client]*Host

func RodeoServer() {
	port := flag.String("port", "9595", "Specify listening port")
	flag.Parse()
	hostConnections = NewHosts()
	clientConnected = make(map[*wserver.Client]*Host)
	log.Println("Server started on port: ", *port)
	wserver.WebsocketServer(newMessage, getOnClose(), *port)
}

func getOnClose() wserver.OnCloseFunc {
	return func(client *wserver.Client) {
		log.Println("Close: ", client.Conn.RemoteAddr())

		//if host closed
		host := hostConnections.GetByConn(client)
		if host != nil {
			hostConnections.Delete(host)
			for key, host := range clientConnected {
				if host.Conn == client {
					delete(clientConnected, key)
					break
				}
			}
			log.Println("SERVER: HOST_CLOSE")
			client.SendJson(&Action{"HOST_CLOSE", ""})
			return
		}

		//if client closed
		host, ok := clientConnected[client]
		if ok {
			host.Conn.SendJson(&Action{"CLIENT_CLOSE", ""})
			log.Println("SERVER: CLIENT_CLOSE")
			delete(clientConnected, client)
			host.Active = false
		}
	}
}

func newMessage(msg *wserver.Message, client *wserver.Client) {
	action := Action{}
	if err := msg.ToJson(&action); err != nil {
		log.Println("rodeo.newMessage: wserver.Message.ToJson:", err)
	}

	switch action.Action {
	case "HOST_REGISTER":
		actHostRegister(&action.Data, client)
	case "GET_HOSTS":
		actGetHosts(&action.Data, client)
	case "SELECT_HOST":
		actSelectHost(&action.Data, client)
	case "CLIENT_ACCESS":
		actClientAccess(&action.Data, client)
	case "CLIENT_DENIED":
		actClientDenied(&action.Data, client)
	}
}

func copyMessage(to *wserver.Client) wserver.OnMessageFunc {
	handler := func(msg *wserver.Message, client *wserver.Client) {
		ActionType, r := msg.Type, msg.Reader

		w, err := to.Conn.NextWriter(ActionType)
		if err != nil {
			log.Println("copyMessage: NextWriter:", err)
		}
		if _, err := io.Copy(w, *r); err != nil {
			log.Println("copyMessage: Copy:", err)
		}
		if err := w.Close(); err != nil {
			log.Println("copyMessage: Close():", err)
		}
	}

	return handler
}
