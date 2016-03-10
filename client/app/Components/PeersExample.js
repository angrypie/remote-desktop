import React, { Component } from 'react'
// import Peer from 'peerjs'

class PeersExample extends Component {

	constructor() {
		super()

		const peer = new Peer({key: '94mluu7f4b98jjor'});
		var self = this;
		peer.on('connection', (conn) => {
			this.setState({
				conn: conn,
				btn: 'Send'
			})

			conn.on('data', (data) => {
				this.setState({
					messages: self.state.messages.concat([data])
				})
			})
		})

		peer.on('open', (id) => {
			this.setState({
				id: id,
				peer: peer
			})
			this.render();
		})

	}

	componentWillMount() {
		this.setState({
			id: '...',
			messages: [],
			btn: 'Connect'
		})
	}

	connect(a, b, c, d, e) {
		var self = this;
		const conn = this.state.peer.connect(this.refs.input.value)
		conn.on('open', () => {
			conn.on('data', (data) => {
				self.setState({
					messages: self.state.messages.concat([data])
				})
			})
		})

		this.setState({
			conn: conn,
			btn: 'Send'
		})

		this.refs.input.value = ''
	}

	send() {
		this.state.conn.send(this.refs.input.value);
		this.refs.input.value = ''
	}

	getMessages() {
		return this.state.messages.map((msg, id) => <div key={id}>{msg}</div>)
	}

	render() {
		return(
			<div>
				<div>My id: {this.state.id}</div>
				<input ref="input" type="text"></input>
				<button onClick={this.state.btn === 'Connect' ?
				 (() => this.connect.call(this)) : 
				 (() => this.send.call(this))
				}>{this.state.btn}</button>
				<div>
					<div>Receive messages: </div>
					<div>
						{this.getMessages()}
					</div>
				</div>
			</div>
		)
	}

}

export default PeersExample