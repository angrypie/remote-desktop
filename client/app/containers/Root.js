import React, { Component } from 'react'
import { connect } from 'react-redux'

import SocketManager from '../components/SocketManager'
import Controll from '../components/Controll'




class Root extends Component {
	render() {
		return (
			<div>
				<Controll user={this.props.user} />
				<SocketManager />
			</div>
			)
	}
}

function mapStateToProps(state) {
	console.log(state)
	return {
		user: state.user
	}
}

export default connect(mapStateToProps)(Root)