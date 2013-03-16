package models.db

import scala.slick.driver.H2Driver.simple._

import Database.threadLocalSession

import org.specs2.mutable._

import play.api.db._
import play.api.Play.current
import play.api.test._
import play.api.test.Helpers._

object Rest extends Specification {

	def printService(service:ServiceRest):Unit = {
		println(s"""
			  	id:$service.id 
			  	name:$service.name 
			  	endpoint:$service.endpoint 
			  	trustAll:$service.trustAll 
			  	accept:$service.accept""")
	}

	def testDb[T](block: => T) =
    running(FakeApplication(additionalConfiguration = inMemoryDatabase("test"))) {
    	
    	val database = Database.forDataSource(DB.getDataSource())
	    
	    database withSession {
			block
		}
	}

	"The query" should {

		"print two services in the console" in testDb {

			Query(ServiceRests) foreach { service:ServiceRest =>
		 		printService(service)
		 		
		 		service.id must not beNone
		 		
		 		service.id.map { id =>
		 			id < 3 must beTrue
	 			}
		 	}
		}

		"return two items in result" in testDb {

			val services = for(s <- ServiceRests) yield s
			services.list must have size(2)
		}
	}
}