package scommons.client.controller

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.router.RouterProps.RouterProps
import scommons.react.test.TestSpec

import scala.scalajs.js.Dynamic.literal

class RouteParamsSpec extends TestSpec {

  it should "return PathParams from pathname when pathParams" in {
    //given
    val pathname = "/app/1/2//3/"
    val location = literal("pathname" -> pathname)
    val nativeProps = literal("location" -> location)
    val routerProps = new RouterProps(Props[Unit](nativeProps))
    val routeParams = new RouteParams(routerProps)

    //when
    val result = routeParams.pathParams
    
    //then
    result.path shouldBe pathname
  }
  
  it should "return PathParams from pathname and search when allParams" in {
    //given
    val pathname = "/app/1/2//3/"
    val search = "?testId=123"
    val location = literal("pathname" -> pathname, "search" -> search)
    val nativeProps = literal("location" -> location)
    val routerProps = new RouterProps(Props[Unit](nativeProps))
    val routeParams = new RouteParams(routerProps)

    //when
    val result = routeParams.allParams
    
    //then
    result.path shouldBe s"$pathname$search"
  }
  
  it should "push url to the browser history" in {
    //given
    val pushMock = mockFunction[String, Unit]
    val history = literal("push" -> pushMock)
    val nativeProps = literal("history" -> history)
    val routerProps = new RouterProps(Props[Unit](nativeProps))
    val routeParams = new RouteParams(routerProps)
    val url = "/app/1/2//3/"

    //then
    pushMock.expects(url)

    //when
    routeParams.push(url)
  }
}
