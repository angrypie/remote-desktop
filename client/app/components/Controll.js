import React, { Component } from 'react'
import { connect } from 'react-redux'
import { setUser, getHosts, selectHost } from '../actions'


class Controll extends Component {
	componentWillMount() {
		let { dispatch, server } = this.props
		dispatch(getHosts(server))
	}

	constructor() {
		super()
		this.state = {selectedHost: null}
	}

	avaliableHosts() {
		let { hosts } = this.props
		return hosts.avaliable.map((host, i) =>
			<div className="host" key={i} onClick={ e => {
				e.preventDefault()
				this.setState({selectedHost: host})
			} }>{host}</div>)
	}

	selectHost() {
		let host = this.state.selectedHost
		if (host == null) return
		let { dispatch, server } = this.props
		dispatch(selectHost(host, server))
	}

	render() {
		let { dispatch, hosts } = this.props
		let btnStyle = {
			cursor: "pointer",
			padding: "5px 10px",
			margin: "0 10px",
			border: "1px solid black",
			display: "inline-block"
		}

		let statusLine = {
			display: "inline-block",
			margin: "0 10px"
		}
		return (
			<div className="select-host" >
				<div className="btn" style={btnStyle} onClick={this.selectHost.bind(this)}>Connect</div>
				<div style={statusLine}>Selected host: {this.state.selectedHost || "[not selected]"}</div>
				<div style={statusLine}>Status: {hosts.current == null ? "[not connected]" : "Connected to " + hosts.current.login}</div>
				<div className="hosts">
					{this.avaliableHosts()}
				</div>
			</div>
		)
	}

}

Controll = connect()(Controll)

export default Controll
