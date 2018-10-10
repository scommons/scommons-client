package scommons.client.ui.list

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.scalatest.Succeeded
import scommons.client.test.TestSpec
import scommons.client.test.raw.ReactTestUtils
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.list.ListBoxCss._
import scommons.client.ui.{ButtonImagesCss, ImageLabelWrapper}

import scala.scalajs.js.Dynamic.literal

class ListBoxSpec extends TestSpec {

  it should "call onSelect once and select single item when onClick" in {
    //given
    val onSelect = mockFunction[Set[String], Unit]
    val props = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), onSelect = onSelect)
    val comp = renderIntoDocument(<(ListBox())(^.wrapped := props)())
    val items = scryRenderedDOMComponentsWithClass(comp, listBoxItem)
    items.length shouldBe props.items.size
    val nextSelectIndex = 0

    //then
    onSelect.expects(Set(props.items(nextSelectIndex).id)).once()

    //when & then
    ReactTestUtils.Simulate.click(items(nextSelectIndex), literal(ctrlKey = false, metaKey = false))
    items(nextSelectIndex).className shouldBe s"$listBoxItem $listBoxSelectedItem"

    //when & then
    ReactTestUtils.Simulate.click(items(nextSelectIndex), literal(ctrlKey = false, metaKey = false))
    items(nextSelectIndex).className shouldBe s"$listBoxItem $listBoxSelectedItem"
  }

  it should "select/deselect multiple items when onClick with ctrlKey or metaKey" in {
    //given
    val onSelect = mockFunction[Set[String], Unit]
    val props = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), onSelect = onSelect)
    val comp = renderIntoDocument(<(ListBox())(^.wrapped := props)())
    val items = scryRenderedDOMComponentsWithClass(comp, listBoxItem)
    items.length shouldBe props.items.size

    //then
    onSelect.expects(Set(props.items.head.id))
    onSelect.expects(Set(props.items.head.id, props.items(1).id))
    onSelect.expects(Set(props.items.head.id))
    onSelect.expects(Set.empty[String])

    //when & then
    ReactTestUtils.Simulate.click(items(0), literal(ctrlKey = true, metaKey = false))
    items(0).className shouldBe s"$listBoxItem $listBoxSelectedItem"

    //when & then
    ReactTestUtils.Simulate.click(items(1), literal(ctrlKey = false, metaKey = true))
    items(1).className shouldBe s"$listBoxItem $listBoxSelectedItem"
    
    //when & then
    ReactTestUtils.Simulate.click(items(1), literal(ctrlKey = false, metaKey = true))
    items(1).className shouldBe s"$listBoxItem "

    //when & then
    ReactTestUtils.Simulate.click(items(0), literal(ctrlKey = true, metaKey = false))
    items(0).className shouldBe s"$listBoxItem "
  }

  it should "reset selectedIds when componentWillReceiveProps" in {
    //given
    val prevProps = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ))
    val renderer = createRenderer()
    renderer.render(<(ListBox())(^.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    assertListBox(comp, prevProps)

    val props = prevProps.copy(selectedIds = Set("1"))

    //when
    renderer.render(<(ListBox())(^.wrapped := props)())

    //then
    val compV2 = renderer.getRenderOutput()
    assertListBox(compV2, props)
  }

  it should "render component" in {
    //given
    val props = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2", Some(ButtonImagesCss.accept))
    ))
    val comp = <(ListBox())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertListBox(result, props)
  }

  it should "render component with pre-selected items" in {
    //given
    val props = ListBoxProps(
      List(
        ListBoxData("1", "Test", Some(ButtonImagesCss.acceptDisabled)),
        ListBoxData("2", "Test2"),
        ListBoxData("3", "Test3", Some(ButtonImagesCss.accept))
      ),
      selectedIds = Set("1", "3")
    )
    val comp = <(ListBox())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertListBox(result, props)
  }

  private def assertListBox(result: ComponentInstance, props: ListBoxProps): Unit = {
    val expectedItems = props.items.map { data =>
      val selectedClass = if (props.selectedIds.contains(data.id)) listBoxSelectedItem else ""

      <.div(
        ^.className := s"$listBoxItem $selectedClass"
      )(
        data.image match {
          case None => data.label
          case Some(image) => ImageLabelWrapper(image, Some(data.label))
        }
      )
    }

    assertDOMComponent(result, <.div()(), { items =>
      items.size shouldBe expectedItems.size
      items.zip(expectedItems).foreach { case (resultItem, expectedItemElem) =>
        assertDOMComponent(resultItem, expectedItemElem)
      }

      Succeeded
    })
  }
}
