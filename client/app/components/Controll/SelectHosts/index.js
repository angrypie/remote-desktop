import React from 'react'
import style from './SelectHosts.css'

let Host = ({ host, onClick, selected }) => {
	let status;
	return (
		<div
			className={`btn ${style.host}`}
			onClick={ e => {
				e.preventDefault()
				onClick(host.login)
			}
		}>
			<div className={`${style.name}`}>{host.login}</div>
			<div
				className={`${style.status} ${selected ? style.selected : style.unselected}`}></div>
		</div>
	)
}

let SelectHosts  = ({ hosts, hostSelected, selected }) => {
	let host_divs =  hosts.avaliable.map((host, i) =>
		<Host key={i} host={host} onClick={hostSelected} selected={selected == host.login} />
	)

	return (
		<div className={style.selectHosts}>
			{host_divs}
		</div>
	)
}

export default SelectHosts