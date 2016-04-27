import React from 'react'
import styles from './style.css'

let SelectHosts  = ({ hosts, hostSelected }) => {
	let host_divs =  hosts.avaliable.map((host, i) =>
		<div className={
			`${styles.host} ${host.active ? styles.busy : styles.free}`
		} key={i} onClick={ e => {
			e.preventDefault()
			hostSelected(host.login)
		} }>{host.login}</div>)

	return (
		<div className="select-hosts">
			{host_divs}
		</div>
	)
}

export default SelectHosts