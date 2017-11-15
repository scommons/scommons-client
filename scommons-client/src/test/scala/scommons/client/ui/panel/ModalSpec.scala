package scommons.client.ui.panel

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.ShallowRenderer
import scommons.client.test.raw.TestReactDOM._
import scommons.client.ui.{Buttons, ButtonsPanel, ButtonsPanelProps}
import scommons.client.util.ActionsData
import scommons.react.modal.NativeReactModal

class ModalSpec extends FlatSpec with Matchers with MockFactory {

  "onAfterOpen" should "call onOpen function" in {
    //given
    val onOpen = mockFunction[Unit]
    val props = getModalProps(closable = true, onOpen = onOpen)
    val component = E(Modal())(A.wrapped := props)()

    //then
    onOpen.expects()

    //when
    val result = ShallowRenderer.renderAndGetOutput(component)
    result.props.onAfterOpen()
  }

  "onRequestClose" should "call onClose function" in {
    //given
    val onClose = mockFunction[Unit]
    val props = getModalProps(closable = true, onClose = onClose)
    val component = E(Modal())(A.wrapped := props)()

    //then
    onClose.expects()

    //when
    val result = ShallowRenderer.renderAndGetOutput(component)
    result.props.onRequestClose()
  }

  "rendering" should "render correct props for closable modal" in {
    //given
    val props = getModalProps(closable = true)
    val component = E(Modal())(A.wrapped := props)()

    //when
    val result = ShallowRenderer.renderAndGetOutput(component)

    //then
    result.`type` shouldBe NativeReactModal
    result.props.isOpen shouldBe true
    result.props.shouldCloseOnOverlayClick shouldBe true
    result.props.overlayClassName shouldBe "scommons-modal-overlay"
    result.props.className shouldBe "scommons-modal"
    result.props.children.length shouldBe 3
  }

  it should "render correct props for non-closable modal" in {
    //given
    val props = getModalProps(closable = false)
    val component = E(Modal())(A.wrapped := props)()

    //when
    val result = ShallowRenderer.renderAndGetOutput(component)

    //then
    result.`type` shouldBe NativeReactModal
    result.props.isOpen shouldBe true
    result.props.shouldCloseOnOverlayClick shouldBe false
    result.props.overlayClassName shouldBe "scommons-modal-overlay"
    result.props.className shouldBe "scommons-modal"
    result.props.children.length shouldBe 3
  }

  it should "render closable modal in the DOM" in {
    //given
    val body = "test body"
    val props = getModalProps(closable = true)
    val component = E(Modal())(A.wrapped := props)(body)

    //when
    val result = renderIntoDocument(component)

    //then
    val modal = findRenderedComponentWithType(result, NativeReactModal).portal
    assertDOMElement(findReactElement(modal),
      <div class="ReactModal__Overlay ReactModal__Overlay--after-open scommons-modal-overlay">
        <div class="ReactModal__Content ReactModal__Content--after-open scommons-modal" tabindex="-1">
          <div class="modal-header">
            <button type="button" class="close">×</button>
            <h3>{props.header.get}</h3>
          </div>
          <div class="modal-body">
            {body}
          </div>
          {renderAsXml(ButtonsPanel(), ButtonsPanelProps(
            props.buttons,
            props.actions,
            group = false,
            className = Some("modal-footer")
          ))}
        </div>
      </div>
    )

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }

  it should "render non-closable modal in the DOM" in {
    //given
    val body = "test body"
    val props = getModalProps(closable = false)
    val component = E(Modal())(A.wrapped := props)(body)

    //when
    val result = renderIntoDocument(component)

    //then
    val modal = findRenderedComponentWithType(result, NativeReactModal).portal
    assertDOMElement(findReactElement(modal),
      <div class="ReactModal__Overlay ReactModal__Overlay--after-open scommons-modal-overlay">
        <div class="ReactModal__Content ReactModal__Content--after-open scommons-modal" tabindex="-1">
          <div class="modal-header">
            <h3>{props.header.get}</h3>
          </div>
          <div class="modal-body">
            {body}
          </div>
          {renderAsXml(ButtonsPanel(), ButtonsPanelProps(
            props.buttons,
            props.actions,
            group = false,
            className = Some("modal-footer")
          ))}
        </div>
      </div>
    )

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }

  it should "render modal without header in the DOM" in {
    //given
    val body = "test body"
    val props = getModalProps(closable = true, header = None)
    val component = E(Modal())(A.wrapped := props)(body)

    //when
    val result = renderIntoDocument(component)

    //then
    val modal = findRenderedComponentWithType(result, NativeReactModal).portal
    assertDOMElement(findReactElement(modal),
      <div class="ReactModal__Overlay ReactModal__Overlay--after-open scommons-modal-overlay">
        <div class="ReactModal__Content ReactModal__Content--after-open scommons-modal" tabindex="-1">
          <div class="modal-body">
            {body}
          </div>
          {renderAsXml(ButtonsPanel(), ButtonsPanelProps(
            props.buttons,
            props.actions,
            group = false,
            className = Some("modal-footer")
          ))}
        </div>
      </div>
    )

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }

  private def getModalProps(closable: Boolean,
                            header: Option[String] = Some("test header"),
                            onClose: () => Unit = () => (),
                            onOpen: () => Unit = () => ()): ModalProps = ModalProps(
    show = true,
    header,
    List(Buttons.OK, Buttons.CANCEL),
    ActionsData(Set(Buttons.OK.command, Buttons.CANCEL.command), _ => ()),
    onClose,
    closable,
    onOpen
  )
}
