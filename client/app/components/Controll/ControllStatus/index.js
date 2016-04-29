import React from 'react'
import style from '../Controll.css'

let ControllStatus = ({ current }) => {
	let status = current.conn == null ? "Offline" : "Connected"

	return (
			<div className={`label ${style.status}`}>{status}</div>
	)
}

export default ControllStatus