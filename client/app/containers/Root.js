import React, { Component } from 'react'
import { connect } from 'react-redux'

import Controll from '../components/Controll'




class Root extends Component {
	render() {
		return (
			<div>
				<Controll hosts={this.props.hosts} server="localhost:9595" />
			</div>
			)
	}
}

function mapStateToProps(state) {
	return {
		user: state.user,
		hosts: state.hosts
	}
}

export default connect(mapStateToProps)(Root)