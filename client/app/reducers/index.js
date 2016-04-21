import * as types from '../constants/actions.js'

const initialState = {
	name: "John Doe"
}

function user(state = initialState, action) {
	switch (action.type) {
		case types.SET_USER:
			return {...state, name: action.name}
		default:
			return state
	}
}

const reducers = {
	user
}

export default reducers



