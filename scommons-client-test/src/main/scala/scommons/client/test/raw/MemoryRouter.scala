package scommons.client.test.raw

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-router", "MemoryRouter")
object NativeMemoryRouter extends ReactClass

object MemoryRouter {

  implicit class MemoryRouterVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val MemoryRouter = elements(NativeMemoryRouter)
  }

  object MemoryRouterVirtualDOMAttributes {
    case class EntriesAttributeSpec(name: String) extends AttributeSpec {
      def :=(entries: List[String]) = Attribute[js.Array[String]](name, js.Array(entries: _*), AS_IS)
    }
  }

  implicit class MemoryRouterVirtualDOMAttributes(attribute: VirtualDOMAttributes) {

    import MemoryRouterVirtualDOMAttributes._

    lazy val initialEntries = EntriesAttributeSpec("initialEntries")
    lazy val initialIndex = IntegerAttributeSpec("initialIndex")
    lazy val keyLength = IntegerAttributeSpec("keyLength")
  }
}
