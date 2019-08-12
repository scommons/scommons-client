package scommons.client.ui.popup

import org.scalajs.dom.raw.{EventTarget, HTMLElement}
import org.scalajs.dom.{KeyboardEvent, MouseEvent, document}
import scommons.client.ui.popup.WithAutoHideSpec.DomEventMock
import scommons.react.test.TestSpec
import scommons.react.test.dom.raw.TestReactDOM.unmountComponentAtNode
import scommons.react.test.dom.util.TestDOMUtils

import scala.scalajs.js.annotation.JSExportAll

class WithAutoHideSpec extends TestSpec with TestDOMUtils {

  it should "call onHide when triggered outside content element on mouseUp" in {
    //given
    val onHide = mockFunction[Unit]
    domRender(<(WithAutoHide())(^.wrapped := WithAutoHideProps(onHide))("test content"))

    //then
    onHide.expects()

    //when
    fireDomEvent(document.dispatchEvent(createDomEvent[MouseEvent]("mouseup")))

    //cleanup
    unmountComponentAtNode(domContainer) shouldBe true
  }

  it should "not call onHide when unmounted on mouseUp" in {
    //given
    val onHide = mockFunction[Unit]
    domRender(<(WithAutoHide())(^.wrapped := WithAutoHideProps(onHide))("test content"))
    unmountComponentAtNode(domContainer) shouldBe true

    //then
    onHide.expects().never()

    //when
    fireDomEvent(document.dispatchEvent(createDomEvent[MouseEvent]("mouseup")))
  }

  it should "call onHide when triggered outside content element on keyDown" in {
    //given
    val onHide = mockFunction[Unit]
    domRender(<(WithAutoHide())(^.wrapped := WithAutoHideProps(onHide))("test content"))

    //then
    onHide.expects()

    //when
    fireDomEvent(document.dispatchEvent(createDomEvent[KeyboardEvent]("keydown")))

    //cleanup
    unmountComponentAtNode(domContainer) shouldBe true
  }

  it should "not call onHide when unmounted on keyDown" in {
    //given
    val onHide = mockFunction[Unit]
    domRender(<(WithAutoHide())(^.wrapped := WithAutoHideProps(onHide))("test content"))
    unmountComponentAtNode(domContainer) shouldBe true

    //then
    onHide.expects().never()

    //when
    fireDomEvent(document.dispatchEvent(createDomEvent[KeyboardEvent]("keydown")))
  }

  it should "fail if autoHideDiv is null" in {
    //given
    val onHide = mockFunction[Unit]
    val event = mock[DomEventMock]

    //when
    val e = the[IllegalArgumentException] thrownBy {
      WithAutoHide.onAutoHide(null, onHide)(event.asInstanceOf[MouseEvent])
    }

    //then
    e.getMessage should include ("autoHideDiv should not be null")
  }

  it should "call onHide when event target is outside autoHide element" in {
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

  it should "render the component" in {
    //given
    val content = "test content"

    //when
    domRender(<(WithAutoHide())(^.wrapped := WithAutoHideProps(() => ()))(
      <.p()(content)
    ))

    //then
    assertDOMElement(domContainer, <.div()(
      <.div()(
        <.p()(content)
      )
    ))

    //cleanup
    unmountComponentAtNode(domContainer) shouldBe true
  }
}

object WithAutoHideSpec {

  @JSExportAll
  trait DomEventMock {

    def target: EventTarget
  }
}
