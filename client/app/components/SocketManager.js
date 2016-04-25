import React, { Component } from 'react'
import { connect } from 'react-redux'



class SocketManager extends Component {
	componentWillMount() {
	}


	render() {
		return (
			<div>
			</div>
		)
	}
}

SocketManager = connect()(SocketManager)


export default SocketManager
