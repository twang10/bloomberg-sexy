package controllers

import play.api._
import play.api.mvc._

import views._
import models._

object Application extends Controller {

  def index = Action {
    
    val sexy = new Multiverse("goog")
    sexy.sendGet()
    Ok(views.html.index("Your new application is ready."))

  }

  def sexy(id: String) = Action {
  	Ok(views.html.index(id))
  } 


}