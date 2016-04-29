import React, { Component } from 'react'
import { connect } from 'react-redux'
import style from './Display.css'

class Display extends Component {
	render() {
		let { host } = this.props
		return (
			<div className={style.display}>
				<div className={style.visual}>
					<img src={"data:image/png;base64," + host.frame} />
				</div>
			</div>
		)
	}
}

Display = connect()(Display)

export default Display