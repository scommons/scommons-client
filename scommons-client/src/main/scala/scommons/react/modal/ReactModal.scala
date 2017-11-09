package scommons.react.modal

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{Attribute, AttributeSpec, BooleanAttributeSpec, IntegerAttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportAll, JSImport}

@js.native
@JSImport("react-modal", JSImport.Default)
object NativeReactModal extends ReactClass

@JSExportAll
case class ReactModalStyle()

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
    lazy val style = ReactModalStyleAttributeSpec("style")
    lazy val shouldCloseOnOverlayClick = BooleanAttributeSpec("shouldCloseOnOverlayClick")
  }
}
