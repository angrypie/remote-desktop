import React from 'react'

let ControllStatus = ({ current }) => {
		let statusLine = {
			display: "inline-block",
			margin: "0 10px"
		}
	return (
		<div clasName="controll-status">
			<div style={statusLine}>Status: {current.conn == null ? "[not connected]" : "Connected to " + current.login}</div>
		</div>
	)
}

export default ControllStatus