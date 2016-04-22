import React, { Component } from 'react'
import { connect } from 'react-redux'
import { setUser } from '../actions'


let Controll = ( {user, dispatch} )  => {
	let input

	return (
		<div>
		<h1>Hello {user.name}. Wellcome to remote-desktop app</h1>
			<input type="text" ref={node => input = node} />
			<button onClick={e => {
				e.preventDefault()
				if(!input.value.trim()) {
					return
				}
				dispatch(setUser(input.value))
				input.value = ''
			}}>Set Name</button>
			
		</div>
	)
}

Controll = connect()(Controll)

export default Controll
