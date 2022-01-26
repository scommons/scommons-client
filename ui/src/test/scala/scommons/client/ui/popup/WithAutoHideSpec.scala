package scommons.client.ui.popup

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{Event, MouseEvent}
import scommons.react.test._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

class WithAutoHideSpec extends TestSpec with TestRendererUtils {

  WithAutoHide.addDomListener = null
  WithAutoHide.removeDomListener = null

  it should "not call onHide if triggered inside content element when on(mouseup/keydown)" in {
    //given
    val onHide = mockFunction[Unit]
    val containsMock = mockFunction[HTMLElement, Boolean]
    val divMock = literal("contains" -> containsMock)
    val addDomListener = mockFunction[String, js.Function1[Event, Unit], Unit]
    val removeDomListener = mockFunction[String, js.Function1[Event, Unit], Unit]
    WithAutoHide.addDomListener = addDomListener
    WithAutoHide.removeDomListener = removeDomListener
    
    var onEvent: js.Function1[Event, Unit] = null
    addDomListener.expects("mouseup", *).onCall { (_, listener) =>
      onEvent = listener
    }
    addDomListener.expects("keydown", *).onCall { (_, listener) =>
      listener shouldBe onEvent
    }
    val target = literal().asInstanceOf[HTMLElement]

    //when
    testRender(<(WithAutoHide())(^.wrapped := WithAutoHideProps(onHide))("test content"), { el =>
      if (el.`type` == "div".asInstanceOf[js.Any]) divMock
      else null
    })

    //then
    containsMock.expects(target).returning(true)
    onHide.expects().never()

    //when
    onEvent(literal(target = target).asInstanceOf[Event])
  }

  it should "call onHide if triggered outside content element when on(mouseup/keydown)" in {
    //given
    val onHide = mockFunction[Unit]
    val containsMock = mockFunction[HTMLElement, Boolean]
    val divMock = literal("contains" -> containsMock)
    val addDomListener = mockFunction[String, js.Function1[Event, Unit], Unit]
    val removeDomListener = mockFunction[String, js.Function1[Event, Unit], Unit]
    WithAutoHide.addDomListener = addDomListener
    WithAutoHide.removeDomListener = removeDomListener
    
    var onEvent: js.Function1[Event, Unit] = null
    addDomListener.expects("mouseup", *).onCall { (_, listener) =>
      onEvent = listener
    }
    addDomListener.expects("keydown", *).onCall { (_, listener) =>
      listener shouldBe onEvent
    }
    val target = literal().asInstanceOf[HTMLElement]

    //when
    testRender(<(WithAutoHide())(^.wrapped := WithAutoHideProps(onHide))("test content"), { el =>
      if (el.`type` == "div".asInstanceOf[js.Any]) divMock
      else null
    })

    //then
    containsMock.expects(target).returning(false)
    onHide.expects()

    //when
    onEvent(literal(target = target).asInstanceOf[Event])
  }

  it should "remove listeners when un-mount" in {
    //given
    val onHide = mockFunction[Unit]
    val divMock = literal()
    val addDomListener = mockFunction[String, js.Function1[Event, Unit], Unit]
    val removeDomListener = mockFunction[String, js.Function1[Event, Unit], Unit]
    WithAutoHide.addDomListener = addDomListener
    WithAutoHide.removeDomListener = removeDomListener

    var onEvent: js.Function1[Event, Unit] = null
    addDomListener.expects("mouseup", *).onCall { (_, listener) =>
      onEvent = listener
    }
    addDomListener.expects("keydown", *).onCall { (_, listener) =>
      listener shouldBe onEvent
    }
    val renderer = createTestRenderer(<(WithAutoHide())(^.wrapped := WithAutoHideProps(onHide))("test content"), { el =>
      if (el.`type` == "div".asInstanceOf[js.Any]) divMock
      else null
    })

    //then
    onHide.expects().never()
    removeDomListener.expects("mouseup", onEvent)
    removeDomListener.expects("keydown", onEvent)

    //when
    TestRenderer.act { () =>
      renderer.unmount()
    }
  }

  it should "fail if autoHideDiv is null" in {
    //given
    val onHide = mockFunction[Unit]
    val target = literal().asInstanceOf[HTMLElement]

    //when
    val e = the[IllegalArgumentException] thrownBy {
      WithAutoHide.onAutoHide(null, onHide)(literal(target = target).asInstanceOf[MouseEvent])
    }

    //then
    e.getMessage should include ("autoHideDiv should not be null")
  }

  it should "render component" in {
    //given
    val content = "test content"
    val divMock = literal()
    val addDomListener = mockFunction[String, js.Function1[Event, Unit], Unit]
    val removeDomListener = mockFunction[String, js.Function1[Event, Unit], Unit]
    WithAutoHide.addDomListener = addDomListener
    WithAutoHide.removeDomListener = removeDomListener
    addDomListener.expects("mouseup", *)
    addDomListener.expects("keydown", *)

    //when
    val result = testRender(<(WithAutoHide())(^.wrapped := WithAutoHideProps(() => ()))(
      <.p()(content)
    ), { el =>
      if (el.`type` == "div".asInstanceOf[js.Any]) divMock
      else null
    })

    //then
    assertNativeComponent(result,
      <.div()(
        <.p()(content)
      )
    )
  }
}
