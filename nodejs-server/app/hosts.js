
class Hosts {
	constructor() {
		this.hosts = {}
	}

	add(info, conn) {
		return info.login in this.hosts ? false : this.hosts[info.login] = {
			login: info.login,
			wconn: conn,
			active: false
		}
	}

	getHost(login) {
		return this.hosts[login]
	}

	remove(login) {
		delete this.hosts[login]
	}
}

export default Hosts
