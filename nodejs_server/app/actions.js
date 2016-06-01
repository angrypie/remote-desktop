
const hostRegister = (data, client, hosts) => {
	if( hosts.add({login: data}, client) == false) {
		sendAction("REGISTER_FAIL", "login exist", client)
	} else {
		sendAction("REGISTER_SUCCESS", "", client)
	}
}

const getHosts = (data, client, hosts) => {
	let info =  []
	let list = hosts.hosts
	for(let login in list) {
		info.push({
			login: login,
			active: list[login].active
		})
	}
	sendAction("AVALIABLE_HOSTS", info, client)
}

const selectHosts = (data, client, hosts) => {
	let host = hosts.getHost(data)
	if(host == undefined)
		return sendAction("SELECT_FAIL", "host not exist", client)
		
	if(host.active)
		return sendAction("SELECT_FAIL", "host busy", client)

	sendAction("CLIENT_CONNECT", "", host.wconn)

	host.wconn.clientAccess = () => {
		host.active = true
		sendAction("SELECT_SUCCESS", "", client)

		//Relay between client and host
		host.wconn.onmessage = (msg) => client.conn.send(msg)
		client.onmessage = (msg) => host.wconn.conn.send(msg)
	}

	host.wconn.clientDenied = () => {
		sendAction("SELECT_FAIL", "denied", client)
	}
}

const clientAccess = (data, client) => {
	client.clientAccess()
}

const clientDenied = (data, client) => {
	client.clientDenied()
}


const Actions = {
		"HOST_REGISTER": hostRegister,
		"GET_HOSTS": getHosts,
		"SELECT_HOST": selectHosts,
		"CLIENT_ACCESS": clientAccess,
		"CLIENT_DENIED": clientDenied
}

const getActionHandler = action => Actions[action]


const sendAction = (action, data, client) => {
	client.conn.send(JSON.stringify({
		action: action,
		data: data
	}))
}


export { getActionHandler }
