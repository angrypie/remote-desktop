package server

import (
	"encoding/json"
	"log"
	"net"
	"strconv"
)

type Request struct {
	Action string
	User   string
	Data   string
}

var clients map[string]string

//Start server
func Start() {
	log.Println("Starting")
	clients = map[string]string{}
	socket := initSocket(":6789")

	for {
		handleClient(socket)
	}
}

//Обработка запросов по udp
func handleClient(sock *net.UDPConn) {
	var buf [2048]byte
	len, sender, err := sock.ReadFromUDP(buf[0:])

	if err != nil {
		log.Println(err)
		return
	}
	var request Request
	if err = json.Unmarshal(buf[:len], &request); err != nil {
		log.Println(err)
		return
	}

	switch request.Action {
	case "INIT":
		initAction(request, sender, sock)
	case "CONNECT":
		connectAction(request, sender, sock)
	}
}

//Обработка запроса req: INIT
//Отправитель sender получит запрос INIT_DONE.
func initAction(req Request, sender *net.UDPAddr, sock *net.UDPConn) {
	remoteAddr := sender.IP.String() + ":" + strconv.Itoa(sender.Port)
	log.Println(sender.Port)
	clients[req.User] = remoteAddr
	sendRequest("INIT_DONE", req.User, remoteAddr, sock, sender)
}

//Обработка запроса req: CONNECT
//Отправитель sender получит запрос USER.
func connectAction(req Request, sender *net.UDPAddr, sock *net.UDPConn) {
	addr, ok := clients[req.Data]
	if !ok {
		addr = "undefined"
	}
	sendRequest("USER", req.Data, addr, sock, sender)
}

//Создать структуру Request action, user и data.
//Отправить запрос по адресу dest, используя udp соединения sock
func sendRequest(action, user, data string, sock *net.UDPConn, dest *net.UDPAddr) {
	answer, err := json.Marshal(&Request{action, user, data})
	if err != nil {
		log.Println(err)
		return
	}
	if _, err = sock.WriteToUDP(answer, dest); err != nil {
		log.Println(err)
		return
	}
}

//Инициализация сокета, слушать по адресу addr
func initSocket(addr string) *net.UDPConn {
	var socket *net.UDPConn
	localAddr, _ := net.ResolveUDPAddr("udp4", addr)

	socket, err := net.ListenUDP("udp4", localAddr)
	if err != nil {
		log.Fatal(err)
	}
	return socket
}
