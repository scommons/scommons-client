package scommons.play.controllers

import java.net.URLEncoder

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import play.api._
import play.api.routing.SimpleRouter
import play.api.test.FakeRequest
import play.api.test.Helpers._

class CommonSwaggerControllerSpec extends FlatSpec
  with Matchers
  with ScalaFutures
  with OneAppPerSuiteWithComponents {

  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(100, Millis))

  override def components: BuiltInComponentsFromContext = new BuiltInComponentsFromContext(context)
    with NoHttpFiltersComponents {

    import play.api.mvc.Results
    import play.api.routing.Router
    import play.api.routing.sird._

    lazy val router: Router = SimpleRouter({
      case GET(p"/") => defaultActionBuilder {
        Results.Ok("success!")
      }
    })
  }

  private lazy val controller = new CommonSwaggerController(components.controllerComponents)

  it should "redirect to swagger ui html page in assets" in {
    //given
    val prefix = "/app-prefix"

    //when
    val result = controller.swaggerUi(prefix).apply(FakeRequest()).futureValue

    //then
    result.header.status shouldBe SEE_OTHER
    result.header.headers(LOCATION) shouldBe {
      s"$prefix/assets/lib/swagger-ui/index.html?url=${URLEncoder.encode(s"$prefix/api-docs", "utf-8")}"
    }
  }
}
