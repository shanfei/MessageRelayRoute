package models

import scala.collection.mutable.{ListBuffer, Queue, LinkedList}
import play.api.libs.json._
import models.Output
import play.api.libs.json.JsSuccess
import models.OutputError
import scala.Some

/**
 * Created with IntelliJ IDEA.
 * User: shanfei
 * Date: 10/13/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
class MessageRelayCategory(category_p:String,relaySubNets_p:String,messageThroughput_p:Int,cost_p:Double) {
   val category =  category_p
   val relaySubNet = relaySubNets_p
   val messageThroughput = messageThroughput_p
   val cost = cost_p
   val subNetNumbers = 254
}

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

//TODO:
object Output {

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


object dispatcher {

    var messageRelayCategories = List(new MessageRelayCategory("SUPER","10.0.4.0/24",25,0.25),
                                      new MessageRelayCategory("LARGE","10.0.3.0/24",10,0.10),
                                      new MessageRelayCategory("MEDIUM","10.0.2.0/24",5,0.05),
                                      new MessageRelayCategory("SMALL","10.0.1.0/24",1,0.01)
                                    )


    def dispatch(input_p:Option[Input]):OutputT = {

        if (input_p == None) {
          return OutputError(1,"request input is null","request input is null")
        }

        var input = input_p.get
        var output:Output = Output()

        output.message = Some(input.message)

        var telNumbers = input.telNumbers.size

        messageRelayCategories.foreach(
            {  messageRelayCategory =>

               val neededSubNetNumbers =  telNumbers / messageRelayCategory.messageThroughput
               if (neededSubNetNumbers > 0) {
                    telNumbers = telNumbers % messageRelayCategory.messageThroughput

                    //build output
                    val recipients = ListBuffer[String]();
                    for (i <- 0 until (neededSubNetNumbers * messageRelayCategory.messageThroughput)) {
                      recipients += input.telNumbers.remove(0)
                    }


                    val route = new NetRoute(messageRelayCategory.relaySubNet.replace("0/24","1")
                      ,recipients)
                    output.routes += route
               }
            }
        )

        output


    }
}
