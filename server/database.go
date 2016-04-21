package server

//import (
//"fmt"
//"gopkg.in/mgo.v2"
//"gopkg.in/mgo.v2/bson"
//"log"
//)

//type Hosts struct {
//Name     string
//Password string
//}

//var rodeodb *mgo.Database

//func initDb() bool {
//dbSession, err := mgo.Dial("localhost")
//if err != nil {
//log.Println("initDb: ", err)
//return false
//}

//db, err := getValidDb(dbSession)
//rodeodb = db
//return true
//}

//func getValidDb(session *mgo.Session) (*mgo.Database, error) {
//db := session.DB("rodeo")
//c := db.C("hosts")

//result := Hosts{}
//err := c.Find(bson.M{"name": "admin"}).One(&result)
//if err != nil {
//log.Println("Not find")
//}
//fmt.Println(result.Name, "\t", result.Password)
//return db, nil
//}
