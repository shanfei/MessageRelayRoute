package controllers

import play.api._
import play.api.mvc._
import models.{dispatcher, Input}
import play.api.libs.json
import play.api.libs.json.Json

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def messageRelay = Action { request =>
     //read json
     val jsonRequest = request.body.asJson.get
     val input =  jsonRequest.as[Input]
     val output = dispatcher.dispatch(Some(input))
     Ok(Json.toJson(output))
  }


  
}