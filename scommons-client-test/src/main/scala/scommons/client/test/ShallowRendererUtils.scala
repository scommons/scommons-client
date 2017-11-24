package scommons.client.test

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.scalatest.Matchers
import scommons.client.test.raw.ShallowRenderer
import scommons.client.test.raw.ShallowRenderer._

import scala.scalajs.js

trait ShallowRendererUtils extends Matchers {

  private val expectNoChildren: List[ComponentInstance] => Unit = { children =>
    children shouldBe Nil
  }

  def createRenderer(): ShallowRenderer = new ShallowRenderer

  def shallowRender(element: js.Object): ComponentInstance = renderAndGetOutput(element)

  def getComponentProps[T](component: ComponentInstance): T = component.props.wrapped.asInstanceOf[T]

  def findComponentWithType(component: ComponentInstance,
                            componentClass: ReactClass): ComponentInstance = {

    def search(components: List[ComponentInstance]): Option[ComponentInstance] = components match {
      case Nil => None
      case head :: tail =>
        if (head.`type` == componentClass) Some(head)
        else {
          search(getComponentChildren(head)) match {
            case None => search(tail)
            case comp => comp
          }
        }
    }

    search(List(component)) match {
      case Some(comp) => comp
      case None =>
        fail(s"Component with type: $componentClass not found")
    }
  }

  def assertComponent[T](result: ComponentInstance,
                         expectedClass: ReactClass,
                         assertProps: T => Unit,
                         assertChildren: List[ComponentInstance] => Unit = expectNoChildren): Unit = {

    result.`type` shouldBe expectedClass

    assertProps(result.props.wrapped.asInstanceOf[T])
    assertChildren(getComponentChildren(result))
  }

  def assertDOMComponent(result: ComponentInstance,
                         expectedElement: Element,
                         assertChildren: List[ComponentInstance] => Unit = expectNoChildren): Unit = {

    result.`type` shouldBe expectedElement.name

    for (attr <- expectedElement.flattenedAttributes) {
      result.props.selectDynamic(attr.name).asInstanceOf[Any] match {
        case resultValue: String => resultValue shouldBe attr.valueToString
        case resultValue => resultValue shouldBe attr.value
      }
    }

    expectedElement.flattenedContents match {
      case Seq(expectedChild) =>
        result.props.children shouldBe expectedChild
      case _ =>
        assertChildren(getComponentChildren(result))
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
