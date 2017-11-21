package scommons.client.ui.popup

import org.scalajs.dom.raw.{EventTarget, HTMLElement}
import org.scalajs.dom.{KeyboardEvent, MouseEvent, document}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.TestReactDOM._

import scala.scalajs.js.annotation.JSExportAll

class WithAutoHideSpec extends FlatSpec with Matchers with MockFactory {

  "mouseUp" should "call onHide when triggered outside content element" in {
    //given
    val onHide = mockFunction[Unit]
    val comp = renderIntoDocument(
      E(WithAutoHide())(A.wrapped := WithAutoHideProps(onHide))("test content")
    )

    //then
    onHide.expects()

    //when
    document.dispatchEvent(createDomEvent[MouseEvent]("mouseup"))

    //cleanup
    unmountComponentAtNode(findDOMNode(comp).parentNode) shouldBe true
  }

  it should "not call onHide when unmounted" in {
    //given
    val onHide = mockFunction[Unit]
    val comp = renderIntoDocument(
      E(WithAutoHide())(A.wrapped := WithAutoHideProps(onHide))("test content")
    )
    unmountComponentAtNode(findDOMNode(comp).parentNode) shouldBe true

    //then
    onHide.expects().never()

    //when
    document.dispatchEvent(createDomEvent[MouseEvent]("mouseup"))
  }

  "keyUp" should "call onHide when triggered outside content element" in {
    //given
    val onHide = mockFunction[Unit]
    val comp = renderIntoDocument(
      E(WithAutoHide())(A.wrapped := WithAutoHideProps(onHide))("test content")
    )

    //then
    onHide.expects()

    //when
    document.dispatchEvent(createDomEvent[KeyboardEvent]("keyup"))

    //cleanup
    unmountComponentAtNode(findDOMNode(comp).parentNode) shouldBe true
  }

  it should "not call onHide when unmounted" in {
    //given
    val onHide = mockFunction[Unit]
    val comp = renderIntoDocument(
      E(WithAutoHide())(A.wrapped := WithAutoHideProps(onHide))("test content")
    )
    unmountComponentAtNode(findDOMNode(comp).parentNode) shouldBe true

    //then
    onHide.expects().never()

    //when
    document.dispatchEvent(createDomEvent[KeyboardEvent]("keyup"))
  }

  "onAutoHide" should "call onHide when event target is outside autoHide element" in {
    //given
    val onHide = mockFunction[Unit]
    val child = document.createElement("div").asInstanceOf[HTMLElement]
    val parent = document.createElement("p").asInstanceOf[HTMLElement]
    parent.appendChild(child)
    val event = mock[DomEventMock]

    //then
    (event.target _).expects().returning(parent)
    onHide.expects()

    //when
    WithAutoHide.onAutoHide(child, onHide)(event.asInstanceOf[MouseEvent])
  }

  it should "not call onHide when event target is inside autoHide element" in {
    //given
    val onHide = mockFunction[Unit]
    val child = document.createElement("div").asInstanceOf[HTMLElement]
    val parent = document.createElement("p").asInstanceOf[HTMLElement]
    parent.appendChild(child)
    val event = mock[DomEventMock]

    //then
    (event.target _).expects().returning(child)
    onHide.expects().never()

    //when
    WithAutoHide.onAutoHide(parent, onHide)(event.asInstanceOf[MouseEvent])
  }

  "rendering" should "render the component" in {
    //given
    val content = "test content"
    val component = E(WithAutoHide())(A.wrapped := WithAutoHideProps(() => ()))(
      E.p()(content)
    )

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div>
        <p>{content}</p>
      </div>
    )

    //cleanup
    unmountComponentAtNode(findDOMNode(result).parentNode) shouldBe true
  }
}

@JSExportAll
trait DomEventMock {

  def target: EventTarget
}
