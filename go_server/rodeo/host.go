package rodeo

import (
	"github.com/angrypie/remote-desktop/go_server/rodeo/wserver"
	"sync"
)

type Host struct {
	Login  string
	Conn   *wserver.Client
	Active bool
	cond   *sync.Cond
	lock   bool
}

func NewHost(login string, client *wserver.Client) *Host {
	newHost := &Host{login, client, false, sync.NewCond(new(sync.Mutex)), false}
	return newHost
}

func (h *Host) Lock() {
	h.lock = true
	h.cond.L.Lock()
}

func (h *Host) Unlock() {
	if h.lock != false {
		h.cond.L.Unlock()
		h.lock = false
	}
}

func (h *Host) Wait() {
	h.cond.Wait()
}

func (h *Host) Signal() {
	h.cond.Signal()
}

type Hosts struct {
	hosts map[string]*Host
}

func NewHosts() *Hosts {
	hosts := Hosts{make(map[string]*Host)}

	return &hosts
}

//Information about host for represent in JSON
type hostInfo struct {
	Login  string `json:"login"`
	Active bool   `json:"active"`
}

var hostConnections *Hosts

//Returns: true if host successfully added, and
//false if host with this login alredy exist
func (h *Hosts) Add(host *Host) bool {
	if _, ok := h.hosts[host.Login]; ok {
		return false
	}

	h.hosts[host.Login] = host
	return true
}

func (h *Hosts) Delete(host *Host) {
	delete(h.hosts, host.Login)
}

//Number of host connections
func (h *Hosts) Size() int {
	return len(h.hosts)
}

func (h *Hosts) getInfo() *[]hostInfo {
	hosts := make([]hostInfo, h.Size())
	i := 0
	for _, host := range h.hosts {
		hosts[i] = hostInfo{host.Login, host.Active}
		i++
	}
	return &hosts
}

func (h *Hosts) GetByLogin(login string) (host *Host, ok bool) {
	host, ok = h.hosts[login]
	return host, ok
}

func (h *Hosts) GetByConn(conn *wserver.Client) (host *Host, ok bool) {
	for _, host := range h.hosts {
		if host.Conn == conn {
			return host, true
		}
	}
	return nil, false
}
