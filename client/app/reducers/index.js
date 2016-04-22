import * as types from '../constants/actions.js'

const initialState = {
	user: {
		name: "John Doe"
	},

	hosts: {
		avaliable: []
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
		default:
			return state
	}
}

const reducers = {
	user,
	hosts
}

export default reducers



