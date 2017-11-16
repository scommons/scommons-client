package scommons.client.test

import io.github.shogowada.statictags.Element
import org.scalatest.Matchers
import scommons.client.test.raw.ShallowRenderer._

import scala.scalajs.js

trait ShallowRendererUtils extends Matchers {

  def shallowRender(element: js.Object): ComponentInstance = renderAndGetOutput(element)

  def assertShallowRenderedElement(result: ComponentInstance, expectedElement: Element): Unit = {
    result.`type` shouldBe expectedElement.name

    for (attr <- expectedElement.flattenedAttributes) {
      result.props.selectDynamic(attr.name).asInstanceOf[Any] match {
        case resultValue: String => resultValue shouldBe attr.valueToString
        case resultValue => resultValue shouldBe attr.value
      }
    }
  }
}
