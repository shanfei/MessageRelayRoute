package models

import play.api.libs.json._
import scala.collection.mutable.ListBuffer
import play.api.libs.json.JsSuccess
import scala.Some

/**
 * Created with IntelliJ IDEA.
 * User: shanfei
 * Date: 10/14/13
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
object Input {
  implicit object InputFormat extends Format[Input] {
    def reads(json:JsValue): JsResult[Input] = {
      val message = (json \ "message").as[String]
      val recipients = (json \ "recipients").as[List[String]].to[ListBuffer]
      JsSuccess(Input(message,recipients))
    }

    def writes(s:Input):JsValue = {

      val retJson = Json.toJson(Map(
        "message" -> s.message,
        "recipients" -> s.telNumbers
      ))

      retJson
    }
  }
}

//TODO:

object Output {
  implicit object OutputFormat extends Format[Output] {
     def reads(json:JsValue): JsResult[Output] = {
       val message = (json \ "message").as[String]
       val routes_p =  (json \ "routes").as[List]
       val routes = ListBuffer[NetRoute]
       routes_p.foreach({
          route =>
           val ip = (route \ "ip").as[String]

       })
     }

     def writes = Json.writes[Output]
  }
}

case class Input(message:String, telNumbers:ListBuffer[String])

class NetRoute(ip_p:String,recipient_p:ListBuffer[String]) {
  var ip = ip_p
  var recipients = recipient_p
}

sealed trait OutputT {
  var message:Option[String] = None
  var routes:ListBuffer[NetRoute] = ListBuffer[NetRoute]()
  var reason:Option[String] = None
  var code:Int = 0
}

case class Output() extends OutputT

case class OutputError(code_p:Int,message_p:String,reason_p:String) extends OutputT {
  reason = Some(reason_p)
  message = Some(message_p)
  code = code_p
}

