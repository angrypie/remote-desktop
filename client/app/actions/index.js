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


export function setUser(name) {
	return {
		type: types.SET_USER,
		name: name
	}
}