package models.rest

import scala.concurrent.duration._
import scala.concurrent.Future

import akka.actor._
import akka.util.Timeout
import akka.pattern.ask
import akka.actor.Props

import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.concurrent.Akka
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.functional.syntax._
  

trait Event


case class Header(name: String, value: Option[String])

case class Request(requestId:Int, path: String, headers: Seq[Header], content:String) extends Event

case class Response(requestId:Int, path: String, headers: Seq[Header], content:String) extends Event

case class Live(enumerator:Enumerator[Event])

class RestServerActor extends Actor {

	implicit val timeout = Timeout(5 seconds)

	val (requestEnumerator, requestChannel) = Concurrent.broadcast[Event]
 
	def receive = {

		case "live" => sender ! Live(requestEnumerator)
    	
    	case req:Request =>  requestChannel.push(req)

    	case resp:Response =>  requestChannel.push(resp)
    	
    	case _ => sender ! "Unexpected message !"
	}
}

object RestServer {

	implicit val timeout = Timeout(5 seconds)

	val restServerActor = Akka.system.actorOf(Props[RestServerActor])

	def live() = {
	
		(restServerActor ? "live").map {
			case Live(enumerator) => enumerator
  		}

  	}

  	def send(event:Event) = restServerActor ! event
  	
}