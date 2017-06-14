package scommons.client.test

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * See https://facebook.github.io/react/docs/test-utils.html
  */
@JSImport("react-dom/test-utils", JSImport.Namespace, "React.addons.TestUtils")
@js.native
object ReactTestUtils extends js.Object {

  type TreeInstance = js.Object with js.Dynamic

  def renderIntoDocument(element: js.Object): TreeInstance = js.native

  def isDOMComponent(instance: js.Object): Boolean = js.native

  def findRenderedDOMComponentWithClass(tree: js.Object, className: String): TreeInstance = js.native
}
