import {createStore, combineReducers, applyMiddleware} from 'redux'
import createSagaMiddleware from 'redux-saga'
import sagas from '../sagas'
import reducers from '../reducers'

const mainReducer = combineReducers({
	...reducers
})

const sagaMiddleware = createSagaMiddleware(sagas)

const store = createStore(
	mainReducer,
	applyMiddleware(sagaMiddleware)
)

export default function configureStore() {
	return store
}


