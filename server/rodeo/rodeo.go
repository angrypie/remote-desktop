package rodeo

import (
	"flag"
	"github.com/angrypie/remote-desktop/server/rodeo/wserver"
	"io/ioutil"
	"log"
)

//client-host relay
var chRelay *Relay

func RodeoServer() {
	port := flag.String("port", "9595", "Specify listening port")
	silent := flag.Bool("silent", false, "Diseble output")
	flag.Parse()

	//disable log output
	if *silent {
		log.SetOutput(ioutil.Discard)
	}

	hostConnections = NewHosts()
	chRelay = NewRelay()
	log.Println("Server started on port: ", *port)
	wserver.WebsocketServer(newMessage, getOnClose(), *port)
}

func getOnClose() wserver.OnCloseFunc {
	return func(conn *wserver.Client) {
		host, ok := chRelay.GetHost(conn)

		//if client closed
		if ok {
			log.Println("\n\nflag--------flag----------flag----------1\n\n")
			chRelay.Delete(conn)
			host.Active = false
			host.Conn.SendJson(&Action{"CLIENT_CLOSE", ""})
			return
		}

		//if host closed
		host, ok = hostConnections.GetByConn(conn)
		if ok {
			//if host was connected to client, delete that record
			//and send HOST_CLOSE to client
			client, ok := chRelay.GetClient(conn)
			if ok {
				log.Println("\n\nflag--------flag----------flag----------2\n\n")
				chRelay.Delete(conn)
				client.SendJson(&Action{"HOST_CLOSE", ""})
			}
			//delete record about host connection
			log.Println("\n\nflag--------flag----------flag----------3\n\n")
			hostConnections.Delete(host)
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
