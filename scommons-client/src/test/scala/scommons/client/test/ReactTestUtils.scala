package scommons.client.test

import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * See https://facebook.github.io/react/docs/test-utils.html
  */
@JSImport("react-dom/test-utils", JSImport.Namespace, "React.addons.TestUtils")
@js.native
object ReactTestUtils extends js.Object {

  def renderIntoDocument(element: ReactElement): ReactElementInstance = js.native
}

@js.native
trait ReactElementInstance extends js.Object
