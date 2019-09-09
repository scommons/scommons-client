package scommons.client.ui.popup

import scommons.client.ui.icon.IconCss
import scommons.client.ui.{HTML, HTMLProps, SimpleButtonData}
import scommons.react.test.TestSpec
import scommons.react.test.raw.ShallowInstance
import scommons.react.test.util.ShallowRendererUtils

class ErrorPopupSpec extends TestSpec with ShallowRendererUtils {

  it should "call onClose function when on close command" in {
    //given
    val onClose = mockFunction[Unit]
    val props = ErrorPopupProps("Some error text", onClose = onClose)
    val component = shallowRender(<(ErrorPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onClose.expects()

    //when
    modalProps.actions.onCommand(_ => ())("close")
  }

  it should "show details when on details command" in {
    //given
    val props = ErrorPopupProps("Some error text", () => (), details = Some("Error details"))
    val renderer = createRenderer()
    renderer.render(<(ErrorPopup())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)

    //when
    modalProps.actions.onCommand(_ => ())("details")

    //then
    assertErrorPopup(renderer.getRenderOutput(), props, showDetails = true)
  }

  it should "hide details when on details command 2nd time" in {
    //given
    val props = ErrorPopupProps("Some error text", () => (), details = Some("Error details"))
    val renderer = createRenderer()
    renderer.render(<(ErrorPopup())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)
    modalProps.actions.onCommand(_ => ())("details")
    assertErrorPopup(renderer.getRenderOutput(), props, showDetails = true)

    //when
    modalProps.actions.onCommand(_ => ())("details")

    //then
    assertErrorPopup(renderer.getRenderOutput(), props, showDetails = false)
  }

  it should "render component without details" in {
    //given
    val props = ErrorPopupProps("Some error text", () => ())
    val component = <(ErrorPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertErrorPopup(result, props, showDetails = false)
  }

  it should "render component with details" in {
    //given
    val props = ErrorPopupProps("Some error text", () => (), details = Some("Error details"))
    val component = <(ErrorPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertErrorPopup(result, props, showDetails = false)
  }

  it should "set focusedCommand when onOpen" in {
    //given
    val props = ErrorPopupProps("Some error text", () => ())
    val renderer = createRenderer()
    renderer.render(<(ErrorPopup())(^.wrapped := props)())
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

  private def assertErrorPopup(result: ShallowInstance, props: ErrorPopupProps, showDetails: Boolean): Unit = {
    val detailsButton = SimpleButtonData("details", if (showDetails) "Details <<" else "Details >>")
    val closeButton = SimpleButtonData("close", "Close", primary = true)
    val buttonsList =
      if (props.details.isDefined) List(detailsButton, closeButton)
      else List(closeButton)

    assertComponent(result, Modal)({
      case ModalProps(header, buttons, actions, _, onClose, closable, _) =>
        header shouldBe None
        buttons shouldBe buttonsList
        actions.enabledCommands shouldBe Set(detailsButton.command, closeButton.command)
        actions.focusedCommand shouldBe None
        onClose shouldBe props.onClose
        closable shouldBe true
    }, { case List(modalChild) =>
      assertNativeComponent(modalChild, <.div(^.className := "row-fluid")(), { case List(img, html) =>
        assertNativeComponent(img, <.img(^.className := IconCss.dialogError, ^.src := "")())
        assertComponent(html, HTML) {
          case HTMLProps(htmlText, wordWrap) =>
            if (showDetails)
              htmlText shouldBe HTML.makeHtmlText(s"${props.error}\n\n${props.details.getOrElse("")}")
            else
              htmlText shouldBe props.error
  
            wordWrap shouldBe false
        }
      })
    })
  }
}
