package scommons.client

import org.scalatest.Matchers

import scala.scalajs.js

object TestUtils extends Matchers {

  def asArray(value: js.Any): js.Array[js.Dynamic] =
    value.asInstanceOf[js.Array[js.Dynamic]]

  def asNode(component: js.Any, nodeName: String): js.Object with js.Dynamic = {
    val node = TestReactDOM.findDOMNode(component)
    node.nodeName.toLowerCase() shouldBe nodeName
    node
  }
}
