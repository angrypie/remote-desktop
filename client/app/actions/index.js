import * as types from '../constants/actions'

export function getHosts(server) {
	return dispatch => {
		const conn = new WebSocket('ws://'+server)
		conn.onopen = () => {
			conn.send(JSON.stringify({
				action: "GET_HOSTS",
				data: ""
			}))
		}

		conn.onmessage = (event) => {
			let {Action, Data} = JSON.parse(event.data)
			if(Action == "AVALIABLE_HOSTS") {
				dispatch({
					type: types.GET_HOSTS,
					hosts: Data
				})
				conn.close()
			}
		}
	}
}

export function selectHost(host, server) {
	return dispatch => {
		const conn = new WebSocket('ws://'+server)
		conn.onopen = () => {
			conn.send(JSON.stringify({
				action: "SELECT_HOST",
				data: host
			}))
		}

		conn.onmessage = (event) => {
			let {Action, Data} = JSON.parse(event.data)
			console.log("GET: ", Action, " ", Data)
			switch (Action) {
				case "SELECT_SUCCESS":
					dispatch({
						type: types.SELECT_HOST,
						host: { login: host, conn: conn }
					})
					conn.send(JSON.stringify({
						action: "PING", data: ""
					}))
					break
			}
		}
	}
}


export function setUser(name) {
	return {
		type: types.SET_USER,
		name: name
	}
}