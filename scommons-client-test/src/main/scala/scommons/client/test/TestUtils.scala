package scommons.client.test

import org.scalajs.dom._
import org.scalatest.Matchers
import scommons.client.test.raw.TestReactDOM

import scala.scalajs.js

object TestUtils extends Matchers {

  def findReactElement(component: js.Any): Element = asElement(TestReactDOM.findDOMNode(component))

  def asElement(node: Node): Element = node.asInstanceOf[Element]

  def assertDOMElement(result: Element, expected: xml.Elem): Unit = {
    assertElement(TestDOMPath(result, result), xml.Utility.trim(expected))
  }

  private def assertElement(path: TestDOMPath, expected: xml.Node): Unit = {
    val node = path.currNode
    val label = normalizeXmlNodeName(expected.label.toLowerCase)
    node.nodeName.toLowerCase shouldBe label

    if (label == "#text") assertTextContent(path, expected)
    else {
      assertAttributes(path, asMap(node.attributes), expected.attributes.asAttrMap)

      assertChildNodes(path, jsArray(node.childNodes).toList, expected.child.toList)
    }
  }

  private def assertClasses(path: TestDOMPath,
                            resultClasses: String,
                            expectedClasses: String): Unit = {

    if (resultClasses != expectedClasses) {
      def normalize(classes: String) = classes.split(' ').map(_.trim).filter(_.nonEmpty)

      val result = normalize(resultClasses)
      val expected = normalize(expectedClasses)

      if (result.toSet != expected.toSet) {
        fail(
          s"""$path  <-- classes don't match
             |got:
             |\t${result.sorted.mkString(" ")}
             |expected:
             |\t${expected.sorted.mkString(" ")}
             |""".stripMargin)
      }
    }
  }

  private def assertAttributes(path: TestDOMPath,
                               result: Map[String, String],
                               expected: Map[String, String]): Unit = {

    val resultKeys = result.keySet - "data-reactroot"
    val expectedKeys = expected.keySet

    if (resultKeys != expectedKeys) {
      fail(
        s"""$path  <-- attribute keys don't match
           |got:
           |\t${result.keys.toList.sorted.mkString("\n\t")}
           |expected:
           |\t${expected.keys.toList.sorted.mkString("\n\t")}
           |""".stripMargin)
    }

    for ((expectedKey, expectedVal) <- expected) {
      val resultVal = result(expectedKey)

      if (expectedKey == "class") {
        assertClasses(path, resultVal, expectedVal)
      }
      else if (resultVal != expectedVal) {
        fail(
          s"""$path  <-- attribute value don't match
             |got:
             |\t$expectedKey = [$resultVal]
             |expected:
             |\t$expectedKey = [$expectedVal]
             |""".stripMargin)
      }
    }
  }

  private def assertChildNodes(path: TestDOMPath,
                               childList: List[Node],
                               expected: List[xml.Node]): Unit = {

    val result = childList.filter(_.nodeName != "#comment")

    val resultTags = result.map(_.nodeName.toLowerCase)
    val expectedTags = expected.map(n => normalizeXmlNodeName(n.label.toLowerCase))
    if (resultTags != expectedTags) {
      fail(
        s"""$path  <-- child tags don't match
           |got:
           |\t${resultTags.mkString("<", ">\n\t<", ">")}
           |expected:
           |\t${expectedTags.mkString("<", ">\n\t<", ">")}
           |""".stripMargin)
    }

    for (i <- expected.indices) {
      val resultNode = result(i)
      val expectedNode = expected(i)

      assertElement(path.at(asElement(resultNode)), expectedNode)
    }
  }

  private def assertTextContent(path: TestDOMPath, expected: xml.Node): Unit = {
    val resultText = path.currNode.textContent
    val expectedText = expected.text

    if (resultText != expectedText) {
      fail(
        s"""$path  <-- text doesn't match
           |got:
           |\t[$resultText]
           |expected:
           |\t[$expectedText]
           |""".stripMargin)
    }
  }

  private def jsArray[T](domList: DOMList[T]): js.Array[T] = {
    if (js.isUndefined(domList)) new js.Array[T](0)
    else {
      val result = new js.Array[T](domList.length)
      for (i <- 0 until domList.length) {
        result(i) = domList(i)
      }

      result
    }
  }

  private def asMap(nodeMap: NamedNodeMap): Map[String, String] = {
    if (nodeMap.length == 0) Map.empty
    else {
      var result = List.empty[(String, String)]
      for (i <- 0 until nodeMap.length) {
        val attr = nodeMap.item(i)
        result = (attr.name, attr.value) :: result
      }

      result.toMap
    }
  }

  private def normalizeXmlNodeName(label: String): String =
    if (label == "#pcdata") "#text"
    else label
}
