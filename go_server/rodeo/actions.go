package rodeo

import (
	"github.com/angrypie/remote-desktop/go_server/rodeo/wserver"
	"log"
)

/*

Client - is client-side application
Host - it is device to which Client are connectiong for remote accesss

*/

//Host want to register on server. data contains host login
//If host successfully registered, than send to client message "RIGESTER_SUCCESS".
//If host with specified login already exist on server, than send "REGISTER_FAIL"
//with "login exist" in data field
func actHostRegister(data *interface{}, client *wserver.Client) {
	hostLogin := (*data).(string)
	new_host := NewHost(hostLogin, client)
	ok := hostConnections.Add(new_host)
	if !ok {
		client.SendJson(&Action{"REGISTER_FAIL", "login exist"})
	} else {
		client.SendJson(&Action{"REGISTER_SUCCESS", ""})
	}
}

//Client requests data about avaliable host.
//Server sends message "AVALIABLE_HOSTS" with hostInfo in data field
func actGetHosts(data *interface{}, client *wserver.Client) {
	hosts := hostConnections.getInfo()
	client.SendJson(&Action{"AVALIABLE_HOSTS", hosts})
}

//Client connects to host. Client and host send messages to each other
//and server doesn't process these messages, just forwards them
func actSelectHost(data *interface{}, client *wserver.Client) {
	name, _ := (*data).(string)
	host, ok := hostConnections.GetByLogin(name)
	if !ok {
		client.SendJson(&Action{"SELECT_FAIL", "host not exist"})
		return
	}
	host.Lock()
	defer func() {
		host.Unlock()
		log.Println("-----------UNLOCK")
	}()

	if host.Active {
		client.SendJson(&Action{"SELECT_FAIL", "host busy"})
		return
	}
	host.Active = true

	host.Conn.SendJson(&Action{"CLIENT_CONNECT", ""})
	log.Println("-----------LOCKED1")
	host.Wait()
	log.Println("-----------LOCKED2")
	if host.Active == false {
		client.SendJson(&Action{"SELECT_FAIL", "denied"})
		return
	}

	host.Conn.SetOnmessage(copyMessage(client))
	client.SetOnmessage(copyMessage(host.Conn))

	chRelay.Add(client, host)
	client.SendJson(&Action{"SELECT_SUCCESS", ""})
}

func actClientAccess(data *interface{}, client *wserver.Client) {
	log.Println("ACEESS")
	host, _ := hostConnections.GetByConn(client)
	host.Active = true
	host.Signal()
}

func actClientDenied(data *interface{}, client *wserver.Client) {
	host, _ := hostConnections.GetByConn(client)
	host.Active = false
	host.Signal()
}
