import * as types from '../constants/actions.js'

const initialState = {
	user: {
		name: "John Doe"
	},

	hosts: {
		avaliable: [],
		current: {
			conn: null,
			streaming: false,
			login: "",
			frame: "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"
		}
	},

	server: {
		addr: "46.149.80.62:9595",
	}
}

function user(state = initialState.user, action) {
	switch (action.type) {
		case types.SET_USER:
			return {...state, name: action.name}
		default:
			return state
	}

}

function hosts(state = initialState.hosts, action) {
	switch (action.type) {
		case types.GET_HOSTS:
			return {...state, avaliable: action.hosts}
		case types.SELECT_HOST:
			if(state.current.conn != null) state.current.conn.close()
			return {...state, current: action.host }
		case types.NEW_FRAME:
			return {...state, current: {frame: action.frame} }
		default:
			return state
	}
}

function server(state = initialState.server, action) {
	return state
}

const reducers = {
	user,
	hosts,
	server
}

export default reducers



