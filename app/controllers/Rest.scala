package controllers

import play.api._
import play.api.mvc.{Controller, Action}
import play.api.libs.{EventSource}

import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.concurrent.Akka

import play.api.Play.current

import scala.concurrent.Await
import akka.pattern.ask
import akka.actor.Props
import akka.util.Timeout
import scala.concurrent.duration._

import play.api.libs.EventSource.EventNameExtractor

import org.joda.time._
import org.joda.time.format._

import scala.slick.driver.H2Driver.simple._

import Database.threadLocalSession

import play.api.db._

import play.api.libs.functional.syntax._

import models.rest._
import models.db._

object Rest extends Controller {

	lazy val database = Database.forDataSource(DB.getDataSource())

	var counter:Int = 0

	val asJson: Enumeratee[Event, JsValue] = Enumeratee.map[Event] { 
		case req:Request => Json.obj("event" -> "request", "requestId" -> req.requestId, "content" -> req.content)
		case resp:Response => Json.obj("event" -> "response", "requestId" -> resp.requestId, "content" -> resp.content)
	}

	implicit val eventNameExtractor = EventNameExtractor[JsValue](eventName = (event) => event.\("event").asOpt[String])

	def live = Action {

		Async {
			RestServer.live.map { 
				enumerator:Enumerator[Event] => Ok.feed(enumerator &> asJson ><> EventSource()).as("text/event-stream")	
			}
		}
	}	

	def req(content:String) = Action {

		counter = counter + 1
		
		RestServer.send(Request(counter, content))
		
		Ok(s"Request $content sent")
	}

	def resp(requestId:String, content:String) = Action {
		
		RestServer.send(Response(requestId.toInt, content))
		
		Ok(s"Response $content sent")
	}

	def datetime() = Action {
		val dateTime = new org.joda.time.DateTime
		Ok(Json.obj("time" -> dateTime.toLocalTime.toString, "date" -> dateTime.toLocalDate.toString))
	}

	def list() = Action {

		implicit val ServiceRestWrites = Json.writes[ServiceRest]
		
		val json = database withSession {
			val serviceRests = for (s <- ServiceRests) yield s
			serviceRests.list
		}

		Ok(Json.obj("result" -> json))
	}
}