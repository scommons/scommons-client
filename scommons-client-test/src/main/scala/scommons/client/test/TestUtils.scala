package scommons.client.test

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom._
import org.scalatest.Matchers
import scommons.client.test.raw.ReactTestUtils.Instance
import scommons.client.test.raw.{ReactTestUtils, TestReactDOM}

import scala.scalajs.js

object TestUtils extends Matchers {

  def createDomEvent[T <: Event](args: js.Any*)(implicit tag: js.ConstructorTag[T]): T = {
    js.Dynamic.newInstance(tag.constructor)(args: _*).asInstanceOf[T]
  }

  def getComponentState[T](component: Instance): T = component.state.wrapped.asInstanceOf[T]

  def getComponentProps[T](component: Instance): T = component.props.wrapped.asInstanceOf[T]

  def findReactElement(component: js.Any): Element = asElement(TestReactDOM.findDOMNode(component))

  private def asElement(node: Node): Element = node.asInstanceOf[Element]

  def renderAsXml(reactClass: ReactClass, props: Any, children: ReactElement*): xml.Elem = {
    domElement2XmlElem(findReactElement(ReactTestUtils.renderIntoDocument(
      React.createElement(reactClass, React.wrap(props), children: _*)
    )))
  }

  def domElement2XmlElem(node: Element): xml.Elem = {
    convertElement(node)
  }

  private def convertElement(node: Node): xml.Elem = {
    val label = node.nodeName.toLowerCase

    val attrs = convertAttributes(getAttributes(node))
    val children = convertChildElements(getChildNodes(node))

    xml.Elem(null, label, attrs, xml.TopScope, minimizeEmpty = false, children: _*)
  }

  private def convertChildElements(childList: List[Node]): List[xml.Node] = childList.map { node =>
    val label = node.nodeName.toLowerCase

    if (label == "#text") xml.Text(node.textContent)
    else convertElement(node)
  }

  private def convertAttributes(attrs: List[(String, String)]): xml.MetaData = {
    @annotation.tailrec
    def loop(attrs: List[(String, String)], next: xml.MetaData): xml.MetaData = attrs match {
      case Nil => next
      case head :: tail =>
        loop(tail, xml.Attribute(None, head._1, xml.Text(head._2), next))
    }

    loop(attrs, xml.Null)
  }

  def assertDOMElement(result: Element, expected: xml.Elem): Unit = {
    assertElement(TestDOMPath(result, result), xml.Utility.trim(expected))
  }

  private def assertElement(path: TestDOMPath, expected: xml.Node): Unit = {
    val node = path.currNode
    val label = normalizeXmlNodeName(expected.label.toLowerCase)
    node.nodeName.toLowerCase shouldBe label

    if (label == "#text") assertTextContent(path, expected)
    else {
      assertAttributes(path, getAttributes(node).toMap, expected.attributes.asAttrMap)

      assertChildNodes(path, getChildNodes(node), expected.child.toList)
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
                               resultAttrs: Map[String, String],
                               expectedAttrs: Map[String, String]): Unit = {

    val result = resultAttrs - "data-reactroot"
    val expected = expectedAttrs - "data-reactroot"

    def mkString(attrs: Map[String, String]): String = {
      attrs.toList.sorted.map(p => s"${p._1}=${p._2}").mkString("\n\t")
    }

    val resultKeys = result.keySet
    val expectedKeys = expected.keySet

    if (resultKeys != expectedKeys) {
      fail(
        s"""$path  <-- attribute keys don't match
           |got:
           |\t${mkString(result)}
           |expected:
           |\t${mkString(expected)}
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

  private def getChildNodes(node: Node): List[Node] = {
    val childNodes = node.childNodes

    if (js.isUndefined(childNodes) || childNodes.length == 0) List.empty[Node]
    else {
      var result = List.empty[Node]
      for (i <- (0 until childNodes.length).reverse) {
        result = childNodes(i) :: result
      }

      result
    }
  }

  private def getAttributes(node: Node): List[(String, String)] = {
    val nodeMap = node.attributes

    if (nodeMap.length == 0) List.empty
    else {
      var result = List.empty[(String, String)]
      for (i <- 0 until nodeMap.length) {
        val attr = nodeMap.item(i)
        result = (attr.name, attr.value) :: result
      }

      result
    }
  }

  private def normalizeXmlNodeName(label: String): String =
    if (label == "#pcdata") "#text"
    else label
}
