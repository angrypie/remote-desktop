import React, { Component } from 'react'
import { connect } from 'react-redux'
import { setUser, getHosts, selectHost } from 'actions'
import SelectHosts from './SelectHosts'
import ControllStatus from './ControllStatus'
import Settings from './Settings'
import Box from './Box'
import style from './Controll.css'


class Controll extends Component {
	componentWillMount() {
		let { dispatch, server } = this.props
		dispatch(getHosts(server))
	}

	constructor() {
		super()
		this.state = {selectedHost: null}
	}

	avaliableHosts() {
		let { hosts } = this.props
		return hosts.avaliable.map((host, i) =>
			<div className="host" key={i} onClick={ e => {
				e.preventDefault()
				this.setState({selectedHost: host})
			} }>{host.login}</div>)
	}

	selectHost() {
		let host = this.state.selectedHost
		if (host == null) return
		let { dispatch, server } = this.props
		dispatch(selectHost(host, server))
	}

	render() {
		let { dispatch, hosts } = this.props

		
		return (
			<div className={style.controll} >
				<Box title="settings">
					<Settings />
				</Box>
				<div className={style.connectWrapper}>
					<div className={`btn ${style.connectBtn}`} onClick={this.selectHost.bind(this)}>Connect</div>
					<ControllStatus current={hosts.current} />
				</div>
				<Box title="avaliable hosts" active={true}>
					<SelectHosts hosts={hosts} 
						hostSelected={(name) => this.setState({selectedHost: name})}
						selected={this.state.selectedHost}/>
				</Box>
			</div>
		)
	}
}

Controll = connect()(Controll)

export default Controll
