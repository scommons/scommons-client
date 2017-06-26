package scommons.client

import org.scalajs.dom.Element
import org.scalajs.dom.Node
import org.scalatest.Matchers

import scala.scalajs.js

object TestUtils extends Matchers {

  def asNode(component: js.Any): Node = TestReactDOM.findDOMNode(component)

  def asElement(node: Node, name: String, childCount: Int = 0): Element = {
    val e = node.asInstanceOf[Element]
    e.nodeName.toLowerCase() shouldBe name
    e.childElementCount shouldBe childCount
    e
  }
}
