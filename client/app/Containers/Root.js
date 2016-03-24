import React, {Component} from 'react'
import SocketManager from '../Components/SocketManager'


export default class Rooa extends Component {
	render() {
		return (
			<div>
				<h1>Remote Desktop</h1>
				<SocketManager />
			</div>
			)
	}
}