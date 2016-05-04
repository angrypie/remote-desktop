import React, { Component } from 'react'
import ReactDOM from 'react-dom'
import { connect } from 'react-redux'
import style from './Display.css'

class Display extends Component {

	constructor() {
		super()
	}

	componentDidMount() {
    let visual = ReactDOM.findDOMNode(this.refs.visual)
    visual.onclick = (event) => {
			let { host } = this.props
    	console.log(host)
    	event.preventDefault()
			host.conn.send(JSON.stringify({
				action: "MOUSE_LCLICK", data: this.state.position
			}))
			host.conn.send(JSON.stringify({
				action: "MOUSE_LRELEASE", data: this.state.position
			}))
    }
    visual.onmousemove = (event) => {
			let { host } = this.props
    	let {top, left} = visual.getBoundingClientRect()
    	let {clientX: x, clientY: y} = event
    	this.setState({
    		position: {
	    		x: x - left,
	    		y: y - top
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
			return <img src={"data:image/png;base64," + host.frame} />
		}
		return <div></div>
	}


	render() {
		return (
			<div onmouseMove={() => this.mousemove()} className={style.display}>
				<div className={style.visual} ref="visual">
					{ this.getFrame() }
				</div>
			</div>
		)
	}
}

Display = connect()(Display)

export default Display