import React, { Component } from 'react'
import { connect } from 'react-redux'
import { getHosts } from '../actions'



class SocketManager extends Component {
	componentWillMount() {
		let { dispatch, server } = this.props
		dispatch(getHosts(server))
	}

	getHosts() {
		let { hosts } = this.props
		return hosts.avaliable.map((host, i) => <div className="host" key={i}>{host}</div>)
	}

	render() {
		return (
			<div>
				<h2>Avaliable host:</h2>
				<div className="hosts">
					{this.getHosts()}
				</div>
				<div className="data">
				</div>
			</div>
		)
	}
}

SocketManager = connect()(SocketManager)


export default SocketManager
