package scommons.client.test

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.scalatest.{Assertion, Matchers, Succeeded}
import scommons.client.test.raw.ShallowRenderer
import scommons.client.test.raw.ShallowRenderer._

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

trait ShallowRendererUtils extends Matchers {

  private val expectNoChildren: List[ComponentInstance] => Assertion = { children =>
    children shouldBe Nil
  }

  def createRenderer(): ShallowRenderer = new ShallowRenderer

  def shallowRender(element: js.Object): ComponentInstance = renderAndGetOutput(element)

  def getComponentProps[T](component: ComponentInstance): T = component.props.wrapped.asInstanceOf[T]

  def findComponents(component: ComponentInstance,
                     componentClass: ReactClass): List[ComponentInstance] = {

    def search(components: List[ComponentInstance],
               result: ListBuffer[ComponentInstance]): Unit = components match {

      case Nil =>
      case head :: tail =>
        if (head.`type` == componentClass) {
          result += head
        }

        search(getComponentChildren(head), result)
        search(tail, result)
    }

    val result = new ListBuffer[ComponentInstance]
    search(List(component), result)
    result.toList
  }

  def assertComponent[T](result: ComponentInstance,
                         expectedClass: ReactClass,
                         assertProps: T => Assertion = (_: T) => Succeeded,
                         assertChildren: List[ComponentInstance] => Assertion = expectNoChildren): Assertion = {

    result.`type` shouldBe expectedClass

    assertProps(result.props.wrapped.asInstanceOf[T])
    assertChildren(getComponentChildren(result))
  }

  def assertDOMComponent(result: ComponentInstance,
                         expectedElement: Element,
                         assertChildren: List[ComponentInstance] => Assertion = expectNoChildren): Assertion = {

    result.`type` shouldBe expectedElement.name

    for (attr <- expectedElement.flattenedAttributes) {
      result.props.selectDynamic(attr.name).asInstanceOf[Any] match {
        case resultValue: String => resultValue shouldBe attr.valueToString
        case resultValue => resultValue shouldBe attr.value
      }
    }

    val children = getComponentChildren(result)

    expectedElement.flattenedContents match {
      case expectedChildren if expectedChildren.nonEmpty =>
        children.size shouldBe expectedChildren.size

        for (i <- expectedChildren.indices) {
          val child = children(i)
          expectedChildren(i) match {
            case expected: Element => assertDOMComponent(child, expected)
            case expected => child shouldBe expected
          }
        }

        Succeeded
      case _ => assertChildren(children)
    }
  }

  private def getComponentChildren(result: ComponentInstance): List[ComponentInstance] = {
    if (js.isUndefined(result.props)) Nil
    else {
      val children = result.props.children

      if (js.isUndefined(children)) Nil
      else if (js.Array.isArray(children)) {
        children.asInstanceOf[js.Array[ComponentInstance]].toList
      }
      else List(children.asInstanceOf[ComponentInstance])
    }
  }
}
