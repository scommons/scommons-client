package scommons.client.controller

import io.github.shogowada.scalajs.reactjs.router.RouterProps.RouterProps

class RouteParams(props: RouterProps) {

  def pathParams: PathParams = PathParams(props.location.pathname)
  
  def allParams: PathParams = {
    val location = props.location
    PathParams(s"${location.pathname}${location.search}")
  }
  
  def push(url: String): Unit = props.history.push(url)
}
