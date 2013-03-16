package models.db

import scala.language.experimental.macros

// Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._

// Use the implicit threadLocalSession
import Database.threadLocalSession

case class ServiceRest(id: Option[Int], name: String, path: String, 
	endpoint: String, username: String, password: String, trustAll: Boolean, accept:String)

object ServiceRests extends Table[ServiceRest]("REST_SERVICE") {

	def id = column[Int]("SERVICE_ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def path = column[String]("PATH")
    def endpoint = column[String]("ENDPOINT")
    def username = column[String]("USERNAME")
    def password = column[String]("PASSWORD")
    def trustAll = column[Boolean]("TRUST_ALL")
    def accept = column[String]("ACCEPT")

    def * = id.? ~ name ~ path ~ endpoint ~ username ~ password ~ trustAll ~ accept <> (ServiceRest, ServiceRest.unapply _)

}

object RestDbService {

	def save = {

	}

}
