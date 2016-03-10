import React, { Component } from 'react'
import Client from 'socket.io-client'



class SocketManager extends Component {
	componentWillMount() {
		this.setState({
			server: "172.16.11.96",
			hosts: []
		})
	}

	getHosts() {
		return this.state.hosts.map((i) => <div className="host">i.name</div>)
	}
	render() {
		return (
			<div>
				<div className="hosts">
					{ this.getHosts() }
				</div>
				<div className="data">
				</div>
			</div>
		)
	}
}

export default SocketManager