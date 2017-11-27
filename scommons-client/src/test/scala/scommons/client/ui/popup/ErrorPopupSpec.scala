package scommons.client.ui.popup

import scommons.client.test.TestSpec
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.icon.IconCss
import scommons.client.ui.{HTML, HTMLProps, SimpleButtonData}

class ErrorPopupSpec extends TestSpec {

  it should "use stackTrace as error details when create props with exception" in {
    //given
    val exception = new Exception("test exception")
    val onClose = () => ()

    //when
    val props = ErrorPopupProps(show = true, "Some error text", exception, onClose)

    //then
    props.show shouldBe true
    props.error shouldBe "Some error text"
    props.onClose shouldBe onClose
    props.details shouldBe Some(ErrorPopup.printStackTrace(exception))
    props.details.get should include ("test exception")
  }

  it should "call onClose function when on close command" in {
    //given
    val onClose = mockFunction[Unit]
    val props = ErrorPopupProps(show = true, "Some error text", onClose = onClose)
    val component = shallowRender(E(ErrorPopup())(A.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onClose.expects()

    //when
    modalProps.actions.onCommand("close")
  }

  it should "show details when on details command" in {
    //given
    val props = ErrorPopupProps(show = true, "Some error text", () => (), details = Some("Error details"))
    val renderer = createRenderer()
    renderer.render(E(ErrorPopup())(A.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)

    //when
    modalProps.actions.onCommand("details")

    //then
    assertErrorPopup(renderer.getRenderOutput(), props, showDetails = true)
  }

  it should "hide details when on details command 2nd time" in {
    //given
    val props = ErrorPopupProps(show = true, "Some error text", () => (), details = Some("Error details"))
    val renderer = createRenderer()
    renderer.render(E(ErrorPopup())(A.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)
    modalProps.actions.onCommand("details")
    assertErrorPopup(renderer.getRenderOutput(), props, showDetails = true)

    //when
    modalProps.actions.onCommand("details")

    //then
    assertErrorPopup(renderer.getRenderOutput(), props, showDetails = false)
  }

  it should "render component without details" in {
    //given
    val props = ErrorPopupProps(show = true, "Some error text", () => ())
    val component = E(ErrorPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertErrorPopup(result, props, showDetails = false)
  }

  it should "render component with details" in {
    //given
    val props = ErrorPopupProps(show = true, "Some error text", () => (), details = Some("Error details"))
    val component = E(ErrorPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertErrorPopup(result, props, showDetails = false)
  }

  it should "set focusedCommand when onOpen" in {
    //given
    val props = ErrorPopupProps(show = true, "Some error text", () => ())
    val renderer = createRenderer()
    renderer.render(E(ErrorPopup())(A.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)
    modalProps.actions.focusedCommand shouldBe None

    //when
    modalProps.onOpen()

    //then
    val updatedComp = renderer.getRenderOutput()
    val updatedModalProps = findComponentProps(updatedComp, Modal)
    updatedModalProps.actions.focusedCommand shouldBe Some("close")
  }

  it should "reset focusedCommand when componentWillReceiveProps" in {
    //given
    val prevProps = ErrorPopupProps(show = true, "Some error text", () => ())
    val renderer = createRenderer()
    renderer.render(E(ErrorPopup())(A.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)
    modalProps.actions.focusedCommand shouldBe None
    modalProps.onOpen()
    val compV2 = renderer.getRenderOutput()
    val modalPropsV2 = findComponentProps(compV2, Modal)
    modalPropsV2.actions.focusedCommand shouldBe Some("close")
    val props = ErrorPopupProps(show = true, "New error text", () => ())

    //when
    renderer.render(E(ErrorPopup())(A.wrapped := props)())

    //then
    val compV3 = renderer.getRenderOutput()
    val modalPropsV3 = findComponentProps(compV3, Modal)
    modalPropsV3.actions.focusedCommand shouldBe None
  }

  private def assertErrorPopup(result: ComponentInstance, props: ErrorPopupProps, showDetails: Boolean): Unit = {
    val detailsButton = SimpleButtonData("details", if (showDetails) "Details <<" else "Details >>")
    val closeButton = SimpleButtonData("close", "Close", primary = true)
    val buttonsList =
      if (props.details.isDefined) List(detailsButton, closeButton)
      else List(closeButton)

    assertComponent(result, Modal(), { modalProps: ModalProps =>
      inside(modalProps) { case ModalProps(show, header, buttons, actions, onClose, closable, _) =>
        show shouldBe props.show
        header shouldBe None
        buttons shouldBe buttonsList
        actions.enabledCommands shouldBe Set(detailsButton.command, closeButton.command)
        actions.focusedCommand shouldBe None
        onClose shouldBe props.onClose
        closable shouldBe true
      }
    }, { case List(modalChild) =>
      assertDOMComponent(modalChild, E.div(^.className := "row-fluid")(), { case List(img, html) =>
        assertDOMComponent(img, E.img(^.className := IconCss.dialogError, ^.src := "")())
        assertComponent(html, HTML(), { htmlProps: HTMLProps =>
          inside(htmlProps) { case HTMLProps(htmlText, wordWrap) =>
            if (showDetails)
              htmlText shouldBe HTML.makeHtmlText(s"${props.error}\n\n${props.details.getOrElse("")}")
            else
              htmlText shouldBe props.error

            wordWrap shouldBe false
          }
        })
      })
    })
  }
}
