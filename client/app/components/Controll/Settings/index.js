import React, { Component } from 'react'
import style from './Settings.css'
import { connect } from 'react-redux'
import Item from './Item'
import { setSettings } from 'actions'


class Settings extends Component {
	constructor() {
		super()
		this.state = {
			settings: {
				interface: ['desktop', 'mobile'],
				connection: ['relay', 'P2P']
			}
		}
	}

	connectionAction(chosen) {
		this.choseSettingOption('connection', chosen)
	}
	
	interfaceAction(chosen) {
		let { dispatch } = this.props
		this.choseSettingOption('interface', chosen)
		dispatch(setSettings({interface: this.state.settings.interface[0]}))
	}

	choseSettingOption(property, option) {
		let arr = this.state.settings[property].concat()
		let elm = arr.splice(option, 1)
		arr.unshift(elm)
		this.setState({settings:{
			...this.state.settings,
			[property]: arr
		}})
	}


	render() {
		return (
				<div className={style.settings}>
					<div className={`label ${style.label}`}>Settings</div>
					<Item
						action={this.interfaceAction.bind(this)}
						name='Interface type'
						options={this.state.settings.interface}
						/>

					<Item
						action={this.connectionAction.bind(this)}
						name='Connection type'
						options={this.state.settings.connection}
						conf={{active: false}}
						/>
				</div>
		)
	}
}

Settings = connect()(Settings)

export default Settings