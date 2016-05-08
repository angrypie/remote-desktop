import React, { Component } from 'react'
import { connect } from 'react-redux'
import style from './Root.css'

import Controll from './Controll'
import Display from './Display'



class Root extends Component {
	render() {
		let {hosts, server, user} = this.props
		return (
			<div className={style.root}>
				<Controll hosts={hosts} server={server.addr} />
				<Display host={hosts.current} />
			</div>
			)
	}
}

function mapStateToProps(state) {
	return {
		user: state.user,
		hosts: state.hosts,
		server: state.server
	}
}

export default connect(mapStateToProps)(Root)