import React, { Component } from 'react'
import style from './Box.css'

class Box extends Component {
	constructor() {
		super()
	}

	componentWillMount() {
		let { active = false} =  this.props
		this.state = {active: active}
	}

	onTitleClick(e) {
		e.preventDefault()
		this.setState({active: !this.state.active})
	}

	render() {
		let { title, children } = this.props
		let { active } = this.state 
		return(
			<div className={`${style.box} ${active ? style.active : style.unactive}`} ref="box">
				<div
				onClick={this.onTitleClick.bind(this)}
				className={`${style.title} `}
				>
					<div>{title}</div>
					<div className={style.indicator}>{active ? '-' : '+'}</div>
				</div>
				{children}
			</div>
		)
	}
}

export default Box