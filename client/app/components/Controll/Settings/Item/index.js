import React from 'react'
import style from './Item.css'

const defaultConf = {
	//if active is false, then item is shown but not responsive
	active: true,
	/// display item or not
	visible: true
}

//Settings item,
//
let Item = ({ name, action, options, conf}) => {
	conf = {...defaultConf, ...conf}
	return !conf.visible ? null : (
		<div className={`label ${style.item} ${conf.active ? '' : style.unactive}`}>
			<div 
				className={style.name}
				>{name}</div>
			<div className={style.options}>{
					options.map((option, key) =>
						<div
							key={key}
							className={`btn ${style.option} `}
							onClick={(e) => {
								e.preventDefault()
								if(!conf.active) return
								action(key)
							}}
							>{option}</div>
					)
				}
			</div>
		</div>
	)
}

export default Item