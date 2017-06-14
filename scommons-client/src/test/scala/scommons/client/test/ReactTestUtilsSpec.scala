package scommons.client.test

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.ReactTestUtils._
import scommons.client.test.ReactTestUtilsSpec._

class ReactTestUtilsSpec extends FlatSpec with Matchers {

  "renderIntoDocument" should "render the component" in {
    //given
    val testProps = TestProps("this is a test")
    val component = React.createElement(testClass, React.wrap(testProps))

    //when
    val result = renderIntoDocument(component)

    //then
    result.props.wrapped shouldBe testProps
  }
}

object ReactTestUtilsSpec {

  val testClass: ReactClass = React.createClass[TestProps, Unit] { (self) =>
    <.div(^.id := "hello-world")(s"Hello, ${self.props.wrapped.test}!")
  }

  case class TestProps(test: String)
}
