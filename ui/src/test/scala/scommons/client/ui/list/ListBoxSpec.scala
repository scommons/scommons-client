package scommons.client.ui.list

import org.scalactic.source.Position
import org.scalajs.dom
import scommons.client.ui.list.ListBoxCss._
import scommons.client.ui.{ButtonImagesCss, ImageLabelWrapper}
import scommons.react._
import scommons.react.test.TestSpec
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.raw.ShallowInstance
import scommons.react.test.util.ShallowRendererUtils

import scala.scalajs.js.Dynamic.literal

class ListBoxSpec extends TestSpec
  with ShallowRendererUtils
  with TestDOMUtils {

  it should "call onSelect once and select single item when onClick" in {
    //given
    val onSelect = mockFunction[Set[String], Unit]
    val props = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), onSelect = onSelect)
    domRender(<(ListBox())(^.wrapped := props)())
    val items = domContainer.querySelectorAll(s".$listBoxItem")
    items.length shouldBe props.items.size
    val nextSelectIndex = 0

    //then
    onSelect.expects(Set(props.items(nextSelectIndex).id)).once()

    //when & then
    fireDomEvent(Simulate.click(items(nextSelectIndex), literal(ctrlKey = false, metaKey = false)))
    items.item(nextSelectIndex).asInstanceOf[dom.Element].getAttribute("class") shouldBe {
      s"$listBoxItem $listBoxSelectedItem"
    }

    //when & then
    fireDomEvent(Simulate.click(items(nextSelectIndex), literal(ctrlKey = false, metaKey = false)))
    items.item(nextSelectIndex).asInstanceOf[dom.Element].getAttribute("class") shouldBe {
      s"$listBoxItem $listBoxSelectedItem"
    }
  }

  it should "select/deselect multiple items when onClick with ctrlKey or metaKey" in {
    //given
    val onSelect = mockFunction[Set[String], Unit]
    val props = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), onSelect = onSelect)
    domRender(<(ListBox())(^.wrapped := props)())
    val items = domContainer.querySelectorAll(s".$listBoxItem")
    items.length shouldBe props.items.size

    //then
    onSelect.expects(Set(props.items.head.id))
    onSelect.expects(Set(props.items.head.id, props.items(1).id))
    onSelect.expects(Set(props.items.head.id))
    onSelect.expects(Set.empty[String])

    //when & then
    fireDomEvent(Simulate.click(items(0), literal(ctrlKey = true, metaKey = false)))
    items.item(0).asInstanceOf[dom.Element].getAttribute("class") shouldBe s"$listBoxItem $listBoxSelectedItem"

    //when & then
    fireDomEvent(Simulate.click(items(1), literal(ctrlKey = false, metaKey = true)))
    items.item(1).asInstanceOf[dom.Element].getAttribute("class") shouldBe s"$listBoxItem $listBoxSelectedItem"
    
    //when & then
    fireDomEvent(Simulate.click(items(1), literal(ctrlKey = false, metaKey = true)))
    items.item(1).asInstanceOf[dom.Element].getAttribute("class") shouldBe s"$listBoxItem "

    //when & then
    fireDomEvent(Simulate.click(items(0), literal(ctrlKey = true, metaKey = false)))
    items.item(0).asInstanceOf[dom.Element].getAttribute("class") shouldBe s"$listBoxItem "
  }

  it should "reset selectedIds when update" in {
    //given
    val prevProps = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ))
    domRender(<(ListBox())(^.wrapped := prevProps)())
    val items = domContainer.querySelectorAll(s".$listBoxItem")
    items.length shouldBe prevProps.items.size
    fireDomEvent(Simulate.click(items(1), literal(ctrlKey = false, metaKey = false)))
    items.item(0).asInstanceOf[dom.Element].getAttribute("class") shouldBe s"$listBoxItem "
    items.item(1).asInstanceOf[dom.Element].getAttribute("class") shouldBe s"$listBoxItem $listBoxSelectedItem"

    val props = prevProps.copy(selectedIds = Set("1"))

    //when
    domRender(<(ListBox())(^.wrapped := props)())

    //then
    items.item(0).asInstanceOf[dom.Element].getAttribute("class") shouldBe s"$listBoxItem $listBoxSelectedItem"
    items.item(1).asInstanceOf[dom.Element].getAttribute("class") shouldBe s"$listBoxItem "
  }

  it should "render component" in {
    //given
    val props = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2", Some(ButtonImagesCss.accept))
    ))

    //when
    val result = shallowRender(<(ListBox())(^.wrapped := props)())

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

    //when
    val result = shallowRender(<(ListBox())(^.wrapped := props)())

    //then
    assertListBox(result, props)
  }

  private def assertListBox(result: ShallowInstance, props: ListBoxProps)(implicit pos: Position): Unit = {
    val expectedItems = props.items.map { data =>
      val selectedClass = if (props.selectedIds.contains(data.id)) listBoxSelectedItem else ""

      <.div(
        ^.className := s"$listBoxItem $selectedClass",
        ^.key := data.id
      )(
        data.image match {
          case None => data.label
          case Some(image) => ImageLabelWrapper(image, Some(data.label))
        }
      )
    }

    assertNativeComponent(result, <.>()(expectedItems))
  }
}
