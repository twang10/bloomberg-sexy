package controllers

import play.api._
import play.api.mvc._

import views._
import models._

object Application extends Controller {

	/* Splash Page & Search */
  def index = Action {

    Ok(views.html.index("Welcome to Bloomberg.sexy"))
  }

  /* Company's Sexy Profile 
		   - takes a company's stock symbal as arg
		   - generates content with multiverse api getter
  */
  def sexy(id: String) = Action {
  	// Company object
    val company = new Multiverse(id)
    company.init()
    company.getTwitter();
    company.getGiphy();
    company.getWiki();
  	company.getBitcoin();
  	company.getReddit();
  	company.getMovies();
  	company.getNYT();
  	company.getPic();
  	
  	Ok(views.html.sexy(company))
  } 
}