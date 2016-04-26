import React, { Component } from 'react'
import { connect } from 'react-redux'

import Controll from '../components/Controll'
import Display from '../components/Display'




class Root extends Component {
	render() {
		return (
			<div>
				<Controll hosts={this.props.hosts} server={this.props.server.addr} />
				<Display host={this.props.hosts.current} />
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