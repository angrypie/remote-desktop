package server

import (
	"net"
	"testing"
)

func TestInitSocket(t *testing.T) {
	addr := ":6789"
	conn := initSocket(addr)

	expectAddr := "0.0.0.0" + addr
	localAddr := conn.LocalAddr().String()
	if expectAddr != localAddr {
		t.Error("Expect: ", expectAddr, "\nGot: ", localAddr)
	}
	conn.Close()
}

func TestInitAction(t *testing.T) {
	addr := ":6789"
	sock := initSocket(addr)
	clients = map[string]string{}
	userName := "User1"
	expectedAddr := "8.8.8.8:8080"
	request := Request{"INIT", userName, ""}
	sender, _ := net.ResolveUDPAddr("udp4", expectedAddr)
	initAction(request, sender, sock)
	if userAddr, ok := clients[request.User]; !ok {
		t.Error("User ", userName, "hasn't added to clients")
	} else {
		if userAddr != expectedAddr {
			t.Error("User ", userName, "has wrong address in clients map",
				"\nExpected: ", expectedAddr,
				"\nGot: ", userAddr)
		}
	}
	sock.Close()
}
