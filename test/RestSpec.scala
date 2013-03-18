package test

import models.rest._

import scala.slick.driver.H2Driver.simple._

import Database.threadLocalSession

import org.specs2.mutable._

import play.api.db._
import play.api.Play.current
import play.api.test._
import play.api.test.Helpers._

object RestDbSpec extends Specification {

	def printService(service:ServiceRest):Unit = {
		println(s"""
			  	id:$service.id 
			  	name:$service.name 
			  	path:$service.path 
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
		 		
		 		service.id.map { id => id < 3 must beTrue

 			}
	 	}
	}

	"return two items in result" in testDb {

			val services = for(s <- ServiceRests) yield s
	
			services.list must have size(2)
		}
	}
}

object RestSpec extends Specification with org.specs2.matcher.ResultMatchers {

	"A rest call" should {

		"return a valid response in proxy mode" in {

			import play.api.libs.json._

			 running(TestServer(9000)) {
        		
        		val realServiceOk = route(FakeRequest(GET, "/datetime")).get

        		status(realServiceOk) mustEqual OK

        		val serviceOk = route(FakeRequest(GET, "/mock/rest/datetime")).get

        		val json: JsValue = Json.parse(contentAsString(serviceOk))

        		(json \ "date").toString() must beMatching("^\"\\d{4}-\\d{2}-\\d{2}\"$")

        		val serviceKo = route(FakeRequest(GET, "/mock/rest/datetimex")).get
        		
    		  	status(serviceKo) must not equalTo OK
			}
			
		}
	}
}