import React, { Component } from 'react'
import { connect } from 'react-redux'


class Display extends Component {
	render() {
		let { host } = this.props
		return (
			<div class="screen">
			{ host.frame == undefined ? <img /> : <img src={"data:image/png;base64," + host.frame} /> }
			</div>
		)
	}
}

Display = connect()(Display)

export default Display