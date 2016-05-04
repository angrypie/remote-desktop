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
			let {action, data} = JSON.parse(event.data)
			if(action == "AVALIABLE_HOSTS") {
				dispatch({
					type: types.GET_HOSTS,
					hosts: data
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
			let {action, data} = JSON.parse(event.data)
			if(action == "IMG_FRAME") {
				console.log("Action:\n", action, "\nData:\n", "img")
			} else {
				console.log("Action:\n", action, "\nData:\n", data)
			}
			switch (action) {
				case "SELECT_SUCCESS":
					dispatch({
						type: types.SELECT_HOST,
						host: { login: host, conn: conn, streaming: true}
					})
					conn.send(JSON.stringify({
						action: "START_STREAM", data: ""
					}))
					break
				case "IMG_FRAME":
					dispatch({
						type: types.NEW_FRAME,
						frame: data
					})
					break
				case "HOST_BUSY":
					conn.close();
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

export function mouseClick(host, type, position) {
	host.current.conn.send(JSON.stringify({
		action: type, data: position
	}))
}