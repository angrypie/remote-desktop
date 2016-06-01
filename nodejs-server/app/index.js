import { InitWsServer } from './wserver'
import { getActionHandler } from './actions'
import Hosts from './hosts'

const hosts = new Hosts()

const clientMessage = (message, client) => {
	let {action, data} = JSON.parse(message)

	let handler = getActionHandler(action)
	if(handler != undefined) handler(data, client, hosts)
}

const clientClose = () => {

}



const serverPort = "9595"

InitWsServer(serverPort, clientMessage, clientClose)

console.log(`Server started on port ${serverPort}`)




