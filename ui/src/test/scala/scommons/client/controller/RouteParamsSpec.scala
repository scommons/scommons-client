package scommons.client.controller

import io.github.shogowada.scalajs.history.History
import io.github.shogowada.scalajs.reactjs.router.Location
import io.github.shogowada.scalajs.reactjs.router.RouterProps.RouterProps
import scommons.client.controller.RouteParamsSpec._
import scommons.react.test.TestSpec

import scala.scalajs.js.annotation.JSExportAll

class RouteParamsSpec extends TestSpec {

  it should "return PathParams from pathname when pathParams" in {
    //given
    val routerProps = mock[RouterProps]
    val location = mock[LocationMock]
    val pathname = "/app/1/2//3/"
    val routeParams = new RouteParams(routerProps)
    
    (routerProps.location _).expects().returning(location.asInstanceOf[Location])
    (location.pathname _).expects().returning(pathname)

    //when
    val result = routeParams.pathParams
    
    //then
    result.path shouldBe pathname
  }
  
  it should "return PathParams from pathname and search when allParams" in {
    //given
    val routerProps = mock[RouterProps]
    val location = mock[LocationMock]
    val pathname = "/app/1/2//3/"
    val search = "?testId=123"
    val routeParams = new RouteParams(routerProps)
    
    (routerProps.location _).expects().returning(location.asInstanceOf[Location])
    (location.pathname _).expects().returning(pathname)
    (location.search _).expects().returning(search)

    //when
    val result = routeParams.allParams
    
    //then
    result.path shouldBe s"$pathname$search"
  }
  
  it should "push url to the browser history" in {
    //given
    val routerProps = mock[RouterProps]
    val history = mock[HistoryMock]
    val url = "/app/1/2//3/"
    val routeParams = new RouteParams(routerProps)

    //then
    (routerProps.history _).expects().returning(history.asInstanceOf[History])
    (history.push _).expects(url)

    //when
    routeParams.push(url)
  }
}

object RouteParamsSpec {

  @JSExportAll
  trait LocationMock {

    def pathname: String
    def search: String
  }
  
  @JSExportAll
  trait HistoryMock {

    def push(url: String): Unit
  }
}
