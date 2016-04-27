import React, { Component } from 'react'
import { connect } from 'react-redux'
import { setUser, getHosts, selectHost } from 'actions'
import SelectHosts from './SelectHosts'
import ControllStatus from './ControllStatus'


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
			} }>{host.login}</div>)
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
		
		return (
			<div className="select-host" >
				<div className="btn" style={btnStyle} onClick={this.selectHost.bind(this)}>Connect</div>
				<SelectHosts hosts={hosts} hostSelected={(name) => this.setState({selectedHost: name})} />
				<ControllStatus current={hosts.current} />
			</div>
		)
	}
}

Controll = connect()(Controll)

export default Controll
