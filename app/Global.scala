import play.api.db.DB
import play.api.GlobalSettings
// Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._

// Use the implicit threadLocalSession
import Database.threadLocalSession

import play.api.Application
import play.api.Play.current

import models.db._


object Global extends GlobalSettings {

  override def onStart(app: Application) {

    lazy val database = Database.forDataSource(DB.getDataSource())

    
    database.withSession {
		ServiceRests.ddl.create
		ServiceRests.insert(ServiceRest(None, "JSON Date & Time", "http://127.0.0.1/datetime", "", "", true, "application/json"))
		ServiceRests.insert(ServiceRest(None, "Say Hello", "http://127.0.0.1/hello", "", "", true, "application/json"))
	}
	

  }
}