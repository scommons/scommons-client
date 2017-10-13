package scommons.client.test.raw

import io.github.shogowada.scalajs.reactjs.React
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestVirtualDOM._

class ShallowRendererSpec extends FlatSpec with Matchers {

  it should "test ShallowRenderer" in {
    //given
    val testElem = React.createElement(React.createClass[Unit, Unit](_ =>
      E.div(A.className := "test-class", A.style := Map("display" -> "none"))(
        E.div.empty,
        E.span()("Hello")
      )
    ))

    //when
    val result = ShallowRenderer.renderAndGetOutput(testElem)

    //then
    result.`type` shouldBe "div"
    result.props.className shouldBe "test-class"
    result.props.style.display shouldBe "none"
    result.props.children.length shouldBe 2
  }
}
