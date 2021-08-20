package scommons.client.ui.popup

import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup.ErrorPopup._
import scommons.client.ui.{HTML, HTMLProps, SimpleButtonData}
import scommons.react._
import scommons.react.test._

class ErrorPopupSpec extends TestSpec with TestRendererUtils {

  ErrorPopup.modalComp = () => "Modal".asInstanceOf[ReactClass]
  ErrorPopup.htmlComp = () => "HTML".asInstanceOf[ReactClass]

  it should "call onClose function when on close command" in {
    //given
    val onClose = mockFunction[Unit]
    val props = ErrorPopupProps("Some error text", onClose = onClose)
    val component = testRender(<(ErrorPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, modalComp)

    //then
    onClose.expects()

    //when
    modalProps.actions.onCommand(_ => ())("close")
  }

  it should "show details when on details command" in {
    //given
    val props = ErrorPopupProps("Some error text", () => (), details = Some("Error details"))
    val renderer = createTestRenderer(<(ErrorPopup())(^.wrapped := props)())
    val comp = renderer.root
    val modalProps = findComponentProps(comp, modalComp)

    //when
    modalProps.actions.onCommand(_ => ())("details")

    //then
    assertErrorPopup(renderer.root, props, showDetails = true)
  }

  it should "hide details when on details command 2nd time" in {
    //given
    val props = ErrorPopupProps("Some error text", () => (), details = Some("Error details"))
    val renderer = createTestRenderer(<(ErrorPopup())(^.wrapped := props)())
    val comp = renderer.root
    val modalProps = findComponentProps(comp, modalComp)
    modalProps.actions.onCommand(_ => ())("details")
    assertErrorPopup(renderer.root, props, showDetails = true)

    //when
    modalProps.actions.onCommand(_ => ())("details")

    //then
    assertErrorPopup(renderer.root, props, showDetails = false)
  }

  it should "render component without details" in {
    //given
    val props = ErrorPopupProps("Some error text", () => ())
    val component = <(ErrorPopup())(^.wrapped := props)()

    //when
    val result = createTestRenderer(component).root

    //then
    assertErrorPopup(result, props, showDetails = false)
  }

  it should "render component with details" in {
    //given
    val props = ErrorPopupProps("Some error text", () => (), details = Some("Error details"))
    val component = <(ErrorPopup())(^.wrapped := props)()

    //when
    val result = createTestRenderer(component).root

    //then
    assertErrorPopup(result, props, showDetails = false)
  }

  it should "set focusedCommand when onOpen" in {
    //given
    val props = ErrorPopupProps("Some error text", () => ())
    val renderer = createTestRenderer(<(ErrorPopup())(^.wrapped := props)())
    val comp = renderer.root
    val modalProps = findComponentProps(comp, modalComp)
    modalProps.actions.focusedCommand shouldBe None

    //when
    modalProps.onOpen()

    //then
    val updatedComp = renderer.root
    val updatedModalProps = findComponentProps(updatedComp, modalComp)
    updatedModalProps.actions.focusedCommand shouldBe Some("close")
  }

  private def assertErrorPopup(result: TestInstance, props: ErrorPopupProps, showDetails: Boolean): Unit = {
    val detailsButton = SimpleButtonData("details", if (showDetails) "Details <<" else "Details >>")
    val closeButton = SimpleButtonData("close", "Close", primary = true)
    val buttonsList =
      if (props.details.isDefined) List(detailsButton, closeButton)
      else List(closeButton)

    assertTestComponent(result.children(0), modalComp)({
      case ModalProps(header, buttons, actions, _, onClose, closable, _) =>
        header shouldBe None
        buttons shouldBe buttonsList
        actions.enabledCommands shouldBe Set(detailsButton.command, closeButton.command)
        actions.focusedCommand shouldBe None
        onClose shouldBe props.onClose
        closable shouldBe true
    }, inside(_) { case List(modalChild) =>
      assertNativeComponent(modalChild, <.div(^.className := "row-fluid")(), inside(_) {
        case List(img, html) =>
          assertNativeComponent(img, <.img(^.className := IconCss.dialogError, ^.src := "")())
          assertTestComponent(html, htmlComp) {
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
