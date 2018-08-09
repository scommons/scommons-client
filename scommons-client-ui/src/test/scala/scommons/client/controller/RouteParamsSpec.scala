package scommons.client.controller

import io.github.shogowada.scalajs.reactjs.router.Location
import io.github.shogowada.scalajs.reactjs.router.RouterProps.RouterProps
import scommons.client.controller.RouteParamsSpec.RoleControllerSpec.LocationMock
import scommons.client.test.TestSpec

import scala.scalajs.js.annotation.JSExportAll

class RouteParamsSpec extends TestSpec {

  it should "return pathname from router location when path" in {
    //given
    val routerProps = mock[RouterProps]
    val location = mock[LocationMock]
    val pathname = "/app/1/2//3/"
    val routeParams = new RouteParams(routerProps)
    
    (routerProps.location _).expects().returning(location.asInstanceOf[Location])
    (location.pathname _).expects().returning(pathname)

    //when
    val result = routeParams.path
    
    //then
    result.value shouldBe pathname
  }
}

object RouteParamsSpec {

  object RoleControllerSpec {

    @JSExportAll
    trait LocationMock {

      def pathname: String
    }
  }
}
