package scommons.client

import org.scalajs.dom._
import org.scalatest.Matchers

import scala.scalajs.js
import scala.xml.{Elem, Utility}

object TestUtils extends Matchers {

  def asNode(component: js.Any): Node = TestReactDOM.findDOMNode(component)

  def asElement(node: Node): Element = node.asInstanceOf[Element]

  def asElement(node: Node, name: String, childCount: Int = 0): Element = {
    val e = asElement(node)
    e.nodeName.toLowerCase() shouldBe name
    e.childElementCount shouldBe childCount
    e
  }

  def asElement(rootElem: Elem): Element = {
    val document = new DOMParser().parseFromString(Utility.trim(rootElem).toString(), "text/html")
    val body = asElement(document.documentElement.getElementsByTagName("body")(0), "body", 1)
    body.firstElementChild
  }

  def asArray[T](domList: DOMList[T]): js.Array[T] = {
    if (js.isUndefined(domList)) new js.Array[T](0)
    else {
      val result = new js.Array[T](domList.length)
      for (i <- 0 until domList.length) {
        result(i) = domList(i)
      }

      result
    }
  }

  def assertDOMElement(result: Element, expected: Element): Unit = {
    assertDOMElement(TestDOMPath(result, result), expected)
  }

  private def assertDOMElement(path: TestDOMPath, expected: Element): Unit = {
    val node = path.currNode
    node.nodeName shouldBe expected.nodeName

    assertClasses(path, asArray(node.classList), asArray(expected.classList))
    assertChildNodes(path, asArray(node.childNodes), asArray(expected.childNodes))
  }

  private def assertClasses(path: TestDOMPath,
                            result: js.Array[String],
                            expected: js.Array[String]): Unit = {

    if (result.toSet != expected.toSet) {
      fail(
        s"""$path  <-- classes doesn't match
           |got:
           |\t${result.sorted.mkString(" ")}
           |expected:
           |\t${expected.sorted.mkString(" ")}
           |""".stripMargin)
    }
  }

  private def assertChildNodes(path: TestDOMPath,
                               childList: js.Array[Node],
                               expected: js.Array[Node]): Unit = {

    val result = childList.filter(_.nodeName != "#comment")
    if (result.length != expected.length) {
      result.map(_.nodeName) shouldBe expected.map(_.nodeName)
    }

    for (i <- expected.indices) {
      val resultNode = result(i)
      val expectedNode = expected(i)

      assertDOMElement(path.at(asElement(resultNode)), asElement(expectedNode))
    }
  }
}
