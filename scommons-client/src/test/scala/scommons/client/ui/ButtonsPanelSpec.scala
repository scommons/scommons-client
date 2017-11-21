package scommons.client.ui

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Inside, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.ui.ButtonImagesCss._
import scommons.client.util.ActionsData

class ButtonsPanelSpec extends FlatSpec
  with Matchers
  with Inside
  with MockFactory
  with ShallowRendererUtils {

  "onClick" should "call onCommand when click on button" in {
    //given
    val onCommand = mockFunction[String, Unit]
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val comp = renderIntoDocument(E(ButtonsPanel())(A.wrapped := ButtonsPanelProps(
      List(data), ActionsData(Set(data.command), onCommand), group = false
    ))())
    val button = findRenderedDOMComponentWithClass(comp, "btn")

    //then
    onCommand.expects(data.command)

    //when
    Simulate.click(button)
  }

  "rendering" should "pass correct props to the children" in {
    //given
    val b1 = SimpleButtonData("accept", "test button 1")
    val b2 = ImageButtonData("add", add, addDisabled, "test button 2")
    val props = ButtonsPanelProps(
      List(b1, b2),
      ActionsData(Set(b1.command), _ => (), Some(b1.command)),
      group = false,
      className = Some("custom-class")
    )
    val component = E(ButtonsPanel())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, E.div(^.className := props.className.get)(), { case List(simpleBtn, imageBtn) =>
      assertComponent(simpleBtn, SimpleButton(), { simpleBtnProps: SimpleButtonProps =>
        inside(simpleBtnProps) { case SimpleButtonProps(data, _, disabled, requestFocus) =>
          data shouldBe b1
          disabled shouldBe false
          requestFocus shouldBe true
        }
      })
      assertComponent(imageBtn, ImageButton(), { imageBtnProps: ImageButtonProps =>
        inside(imageBtnProps) { case ImageButtonProps(data, _, disabled, showTextAsTitle, requestFocus) =>
          data shouldBe b2
          disabled shouldBe true
          showTextAsTitle shouldBe false
          requestFocus shouldBe false
        }
      })
    })
  }

  it should "render buttons toolbar in the DOM" in {
    //given
    val b1 = ImageButtonData("accept", accept, acceptDisabled, "test button 1")
    val b2 = ImageButtonData("add", add, addDisabled, "test button 2")
    val group = false
    val component = E(ButtonsPanel())(A.wrapped := ButtonsPanelProps(
      List(b1, b2), ActionsData(Set(b1.command), _ => ()), group = group
    ))()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div class="btn-toolbar">
        {renderAsXml(ImageButton(), ImageButtonProps(b1, () => (), showTextAsTitle = group))}
        {renderAsXml(ImageButton(), ImageButtonProps(b2, () => (), showTextAsTitle = group, disabled = true))}
      </div>
    )
  }

  it should "render buttons group in the DOM" in {
    //given
    val b1 = ImageButtonData("accept", accept, acceptDisabled, "test button 1")
    val b2 = ImageButtonData("add", add, addDisabled, "test button 2")
    val group = true
    val component = E(ButtonsPanel())(A.wrapped := ButtonsPanelProps(
      List(b1, b2), ActionsData(Set(b1.command), _ => ()), group = group
    ))()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div class="btn-group">
        {renderAsXml(ImageButton(), ImageButtonProps(b1, () => (), showTextAsTitle = group))}
        {renderAsXml(ImageButton(), ImageButtonProps(b2, () => (), showTextAsTitle = group, disabled = true))}
      </div>
    )
  }
}
