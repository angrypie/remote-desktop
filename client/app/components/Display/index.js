import React, { Component } from 'react'
import ReactDOM from 'react-dom'
import { connect } from 'react-redux'
import style from './Display.css'

class Display extends Component {

	constructor() {
		super()
	}

	componentDidMount() {
		let desktop = ReactDOM.findDOMNode(this.refs.desktop)
		if(desktop == null) return
		desktop.onmousedown = (event) => {
			let { host } = this.props
			if (host.conn == null) return
				event.preventDefault()
				host.conn.send(JSON.stringify({
					action: "MOUSE_LPRESS", data: ""
				}))
		}

		desktop.onmouseup = (event) => {
			let { host } = this.props
			if (host.conn == null) return
				event.preventDefault()
				host.conn.send(JSON.stringify({
					action: "MOUSE_LRELEASE", data: ""
				}))
		}
		desktop.onmousemove = (event) => {
			let { host } = this.props
			if (host.conn == null) return
			let {top, left} = desktop.getBoundingClientRect()
			let {clientX: x, clientY: y} = event
			let {width, height, naturalWidth, naturalHeight} = desktop

			this.setState({
				position: {
					x: (naturalWidth / width) * (x - left),
					y: (naturalHeight / height) * (y - top)
				}
			})
			host.conn.send(JSON.stringify({
				action: "MOUSE_MOVE", data: this.state.position
				}))
		}
	}

	getFrame() {
		let { host } = this.props
		if (host.streaming) {
			let img = <img src={"data:image/png;base64," + host.frame} ref="dekstop" /> 
			return img
		}
		return <div></div>
	}


	render() {
		let { host } = this.props
		return (
			<div onmouseMove={() => this.mousemove()} className={style.display}>
				<div className={style.visual} ref="visual">
					<img style={ host.streaming ? {} : {display: "none"}} src={"data:image/png;base64," + host.frame} ref="desktop" />
				</div>
			</div>
		)
	}
}

Display = connect()(Display)

export default Display
