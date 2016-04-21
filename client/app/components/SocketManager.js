import React, { Component } from 'react'



class SocketManager extends Component {
	componentWillMount() {
		this.setState({
			server: "localhost:9595",
			hosts: []
		})

	}

	getHosts() {
		return this.state.hosts.map((i) => <div className="host">i.name</div>)
	}

	render() {
		let socket = new WebSocket("ws://" + this.state.server)
		socket.onopen = () => {
			console.log('Connection established')

			socket.send(JSON.stringify({
				action: "GET_HOSTS",
				data: ""
			}))
		}

		socket.onmessage = (event) =>  {
			console.log("answer: ", event.action, event.data)
			if(event.action == "AVALIABLE_HOSTS") {
				socket.send(JSON.stringify({
					action: "SELECT_HOST",
					data: event.data[0]
				}))
				socket.send(JSON.stringify({
					action: "MOUSE_LCLICK",
					data: "ololo"
				}))
			}
		}
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
