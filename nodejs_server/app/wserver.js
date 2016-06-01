import Ws from 'ws'
import url from 'url'

//key for this.client is string like 8.8.8.8:23232
class Clients {
	constructor(onmessage, onclose) {
		this.onmessage = onmessage
		this.onclose = onclose
		this.clients = {}
	}

	add(key, conn) {
		return this.clients[key] = {
			key: key,
			conn: conn,
			onmessage: this.onmessage,
			onclose: this.onclose
		}
	}

	remove(key) {
		delete this.clients[key]
	}

	exist(key) {
		return this.clients[key] == undefined ? false : true
	}
}

const WServer = (port, onMessage, onClose) => {
	const clients = new Clients(onMessage, onClose)

	const wserver = new Ws.Server({
		port: port
	})

	wserver.on('connection', conn => {
		const {remoteAddress, remotePort} = conn.upgradeReq.connection
		const addr = `${remoteAddress}:${remotePort}`

		if(clients.exist(addr)) {
			return rconn.close()
		}

		const client = clients.add(addr, conn)
			conn.on('message', (message) => {
			client.onmessage(message, client)
		})

		conn.on('close', () => {
			client.onclose(addr)
			clients.remove(client)
		})
	})
}

class Client {

}

const InitWsServer = (port, onMessage, onClose) => {
  WServer(port, onMessage, onClose)
}


export {InitWsServer}
