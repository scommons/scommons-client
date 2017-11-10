package scommons.react.modal

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * Facade for react-modal component
  *
  * @see https://reactcommunity.org/react-modal/
  */
@js.native
@JSImport("react-modal", JSImport.Default)
object NativeReactModal extends ReactClass

trait ReactModalStyleOverlay extends js.Object {
  val position: js.UndefOr[String] = js.undefined
  val top: js.UndefOr[Int] = js.undefined
  val left: js.UndefOr[Int] = js.undefined
  val right: js.UndefOr[Int] = js.undefined
  val bottom: js.UndefOr[Int] = js.undefined
  val backgroundColor: js.UndefOr[String] = js.undefined
}

trait ReactModalStyleContent extends js.Object {
  val position: js.UndefOr[String] = js.undefined
  val top: js.UndefOr[String] = js.undefined
  val left: js.UndefOr[String] = js.undefined
  val right: js.UndefOr[String] = js.undefined
  val bottom: js.UndefOr[String] = js.undefined
  val border: js.UndefOr[String] = js.undefined
  val background: js.UndefOr[String] = js.undefined
  val overflow: js.UndefOr[String] = js.undefined
  val WebkitOverflowScrolling: js.UndefOr[String] = js.undefined
  val borderRadius: js.UndefOr[String] = js.undefined
  val outline: js.UndefOr[String] = js.undefined
  val padding: js.UndefOr[String] = js.undefined
}

trait ReactModalStyle extends js.Object {
  val overlay: js.UndefOr[ReactModalStyleOverlay] = js.undefined
  val content: js.UndefOr[ReactModalStyleContent] = js.undefined
}

object ReactModal {

  implicit class ReactModalVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ReactModal: ReactClassElementSpec = elements(NativeReactModal)
  }

  object ReactModalVirtualDOMAttributes {

    import VirtualDOMAttributes.Type._

    type OnReactModalEvent = js.Function0[Unit]

    case class ReactModalStyleAttributeSpec(name: String) extends AttributeSpec {
      def :=(style: ReactModalStyle): Attribute[ReactModalStyle] =
        Attribute(name = name, value = style, AS_IS)
    }

    case class OnReactModalEventAttribute(name: String) extends AttributeSpec {
      def :=(onEvent: OnReactModalEvent): Attribute[OnReactModalEvent] =
        Attribute(name = name, value = onEvent, AS_IS)
    }
  }

  implicit class ReactModalVirtualDOMAttributes(attributes: VirtualDOMAttributes) {

    import ReactModalVirtualDOMAttributes._

    lazy val isOpen = BooleanAttributeSpec("isOpen")
    lazy val onAfterOpen = OnReactModalEventAttribute("onAfterOpen")
    lazy val onRequestClose = OnReactModalEventAttribute("onRequestClose")
    lazy val closeTimeoutMS = IntegerAttributeSpec("closeTimeoutMS")
    lazy val modalStyle = ReactModalStyleAttributeSpec("style")
    lazy val contentLabel = StringAttributeSpec("contentLabel")
    lazy val overlayClassName = StringAttributeSpec("overlayClassName")
    lazy val modalClassName = StringAttributeSpec("className")
    lazy val shouldCloseOnOverlayClick = BooleanAttributeSpec("shouldCloseOnOverlayClick")
  }
}
