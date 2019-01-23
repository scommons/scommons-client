package scommons.client.ui.select.raw

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * Facade for react-select component
  *
  * @see https://github.com/JedWatson/react-select
  */
@js.native
@JSImport("react-select", JSImport.Default)
object NativeReactSelect extends ReactClass

trait ReactSelectOption extends js.Object {
  val value: js.UndefOr[String] = js.undefined
  val label: js.UndefOr[String] = js.undefined
}

trait ReactSelectAction extends js.Object {
  val action: String
}

object NativeSelect {

  implicit class ReactSelectVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ReactSelect: ReactClassElementSpec = elements(NativeReactSelect)
  }

  object ReactSelectVirtualDOMAttributes {

    import VirtualDOMAttributes.Type._

    type ReactSelectSingleOnChangeEvent = js.Function1[ReactSelectOption, Unit]
    type ReactSelectOnInputChangeEvent = js.Function2[String, ReactSelectAction, Unit]

    case class ReactSelectOptionsAttributeSpec(name: String) extends AttributeSpec {
      def :=(options: List[ReactSelectOption]): Attribute[js.Array[ReactSelectOption]] =
        Attribute(name = name, value = js.Array(options: _*), AS_IS)
    }

    case class ReactSelectSingleOnChangeEventAttribute(name: String) extends AttributeSpec {
      def :=(onEvent: ReactSelectSingleOnChangeEvent): Attribute[ReactSelectSingleOnChangeEvent] =
        Attribute(name = name, value = onEvent, AS_IS)
    }
    
    case class ReactSelectOnInputChangeEventAttribute(name: String) extends AttributeSpec {
      def :=(onEvent: ReactSelectOnInputChangeEvent): Attribute[ReactSelectOnInputChangeEvent] =
        Attribute(name = name, value = onEvent, AS_IS)
    }
  }

  implicit class ReactSelectVirtualDOMAttributes(attributes: VirtualDOMAttributes) {

    import ReactSelectVirtualDOMAttributes._

    lazy val selectedOptions = ReactSelectOptionsAttributeSpec("value")
    lazy val options = ReactSelectOptionsAttributeSpec("options")
    lazy val onSingleSelectChange = ReactSelectSingleOnChangeEventAttribute("onChange")

    lazy val isClearable = BooleanAttributeSpec("isClearable")
    lazy val isDisabled = BooleanAttributeSpec("isDisabled")
    lazy val isLoading = BooleanAttributeSpec("isLoading")
    lazy val isSearchable = BooleanAttributeSpec("isSearchable")
    lazy val onInputChange = ReactSelectOnInputChangeEventAttribute("onInputChange")
    
    lazy val classNamePrefix = StringAttributeSpec("classNamePrefix")
    lazy val menuPlacement = StringAttributeSpec("menuPlacement")
  }
}
