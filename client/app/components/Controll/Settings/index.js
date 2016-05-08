import React, { Component } from 'react'
import style from './Settings.css'
import Item from './Item'


class Settings extends Component {
	constructor() {
		super()
		this.state = {
			settings: {
				interface: ['Desktop', 'Bobile'],
				connection: ['Relay', 'P2P']
			}
		}
	}

	connectionAction(chosen) {
		this.choseSettingOption('connection', chosen)
	}
	
	interfaceAction(chosen) {
		this.choseSettingOption('interface', chosen)
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

export default Settings