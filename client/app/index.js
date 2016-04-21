import 'babel-polyfill'
import "file?name=index.html!./index.html"
import Root from './containers/Root.js'
import configureStore from './store/configureStore.js'

import { Provider } from 'react-redux'
import { createStore } from 'redux'
import React from 'react'
import { render } from 'react-dom'

const store = configureStore()

render(
	<Provider store={store}>
		<Root />
	</Provider>,
	document.getElementById('content')
)





