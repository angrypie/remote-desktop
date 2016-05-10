import React, { Component } from 'react'
import ReactDOM from 'react-dom'
import { connect } from 'react-redux'
import style from './Display.css'

class Display extends Component {

	constructor() {
		super()
		this.state = {handler: false}
	}

	desktopDisplay() {
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

	mobileDisplay() {
		let touchpad  = ReactDOM.findDOMNode(this.refs.touchpad)
		let desktop = ReactDOM.findDOMNode(this.refs.desktop)
		if(touchpad == null) return
		touchpad.addEventListener("touchmove", (event) => {
			let { host } = this.props
			if (host.conn == null) return
			let {top, left} = touchpad.getBoundingClientRect()
			let {clientX: x, clientY: y} = event.touches[0]
			let {naturalWidth, naturalHeight} = desktop
			let {clientHeight: height, clientWidth: width} = touchpad
			this.setState({
				position: {
					x: (naturalWidth / width) * (x - left),
					y: (naturalHeight / height) * (y - top)
				}
			})
			host.conn.send(JSON.stringify({
				action: "MOUSE_MOVE", data: this.state.position
				}))
		}, true);

		touchpad.addEventListener("touchstart", (event) => {
			let { host } = this.props
			if (host.conn == null) return
			console.log('button2')
				event.preventDefault()
				host.conn.send(JSON.stringify({
					action: "MOUSE_LPRESS", data: ""
				}))
		}, true);

		touchpad.addEventListener("touchend", (event) => {
			let { host } = this.props
			if (host.conn == null) return
			console.log('button1')
				event.preventDefault()
				host.conn.send(JSON.stringify({
					action: "MOUSE_LRELEASE", data: ""
				}))
		}, true);

	}

	componentWillUpdate() {
		let { settings, host } = this.props
		if(!host.streaming) return
		if(this.state.handler) return
		if(settings.interface == "desktop") {
			this.desktopDisplay()
		}
		if(settings.interface == "mobile") {
			this.mobileDisplay()
		}
		this.setState({handler: true})
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
		let { host, settings } = this.props
		return (
			<div onmouseMove={() => this.mousemove()} className={style.display}>
				<div className={style.visual} ref="visual">
					<img
						style={ host.streaming ? {} : {display: "none"}}
						src={'data:image/png;base64,'
							+(host.streaming && host.frame != undefined ? host.frame : '')}
						ref="desktop"
						/>
				</div>
				{settings.interface == "mobile" ? <div className={style.touchpad} ref="touchpad"></div> : null}
			</div>
		)
	}
}

Display = connect()(Display)

export default Display
