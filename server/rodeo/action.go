package rodeo

type Action struct {
	Action string      `json:"action"`
	Data   interface{} `json:"data"`
}
