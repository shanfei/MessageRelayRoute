package controllers

import play.api._
import play.api.mvc._
import models.Input
import play.api.libs.json

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def messageRelay = Action { request =>
     //read json
     val jsonRequest = request.body.asJson.get
     val input = json.as[Input]
     Ok
  }
  
}