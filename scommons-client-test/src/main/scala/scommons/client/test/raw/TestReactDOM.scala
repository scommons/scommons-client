package scommons.client.test.raw

import org.scalajs.dom.Node

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-dom", JSImport.Namespace)
object TestReactDOM extends js.Object {

  def findDOMNode(component: js.Any): Node = js.native
}
