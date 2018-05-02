package scommons.client.test.util

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.scalajs.dom.MouseEvent
import org.scalatest.{Failed, OutcomeOf}
import scommons.client.test.TestSpec
import scommons.client.test.util.TestDOMUtils._

class TestDOMUtilsSpec extends TestSpec
  with OutcomeOf {

  it should "return event object when createDomEvent" in {
    //given
    val typeArg = "mouseup"

    //when
    val result = createDomEvent[MouseEvent](typeArg)

    //then
    result.`type` shouldBe typeArg
  }

  it should "fail if classes don't match when assertDOMElement" in {
    //given
    val component = renderIntoDocument(<.div(^.className := "test1 test2")())

    //when
    val Failed(e) = outcomeOf {
      assertDOMElement(findReactElement(component),
        <.div(^("class") := "test")()
      )
    }

    //then
    e.getMessage should include ("classes don't match")
  }

  it should "fail if attribute keys don't match when assertDOMElement" in {
    //given
    val component = renderIntoDocument(<.div(^.className := "test")())

    //when
    val Failed(e) = outcomeOf {
      assertDOMElement(findReactElement(component),
        <.div(^.className := "test")()
      )
    }

    //then
    e.getMessage should include ("attribute keys don't match")
  }

  it should "fail if attribute value don't match when assertDOMElement" in {
    //given
    val component = renderIntoDocument(<.div(^.height := 5)())

    //when
    val Failed(e) = outcomeOf {
      assertDOMElement(findReactElement(component),
        <.div(^.height := 10)()
      )
    }

    //then
    e.getMessage should include ("attribute value don't match")
  }

  it should "fail if child tags don't match when assertDOMElement" in {
    //given
    val component = renderIntoDocument(<.div()())

    //when
    val Failed(e) = outcomeOf {
      assertDOMElement(findReactElement(component),
        <.div()(
          <.p.empty
        )
      )
    }

    //then
    e.getMessage should include ("child tags don't match")
  }

  it should "fail if text doesn't match when assertDOMElement" in {
    //given
    val component = renderIntoDocument(<.div()("test"))

    //when
    val Failed(e) = outcomeOf {
      assertDOMElement(findReactElement(component),
        <.div()("test2")
      )
    }

    //then
    e.getMessage should include ("text doesn't match")
  }

  it should "assert props and children when assertDOMElement" in {
    //given
    val id = System.currentTimeMillis().toString
    val compClass = React.createClass[Unit, Unit] { _ =>
      <.div(
        ^.className := "test1 test2",
        ^.style := Map("display" -> "none"),
        ^.id := id,
        ^.hidden := true,
        ^.height := 10
      )(
        <.div()("child1"),
        <.div()("child2")
      )
    }
    val component = <(compClass)()()
    val result = renderIntoDocument(component)

    //when & then
    assertDOMElement(findReactElement(result),
      <.div(
        ^("class") := "test1 test2",
        ^("style") := "display: none;",
        ^.id := id,
        ^("hidden") := "",
        ^.height := 10
      )(
        <.div()("child1"),
        <.div()("child2")
      )
    )
  }
}
