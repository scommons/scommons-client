package scommons.client.ui.list

import org.scalactic.source.Position
import scommons.client.ui.list.ListBoxCss._
import scommons.client.ui.{ButtonImagesCss, ImageLabelWrapper}
import scommons.react.test._

import scala.scalajs.js.Dynamic.literal

class ListBoxSpec extends TestSpec with TestRendererUtils {

  it should "call onSelect once and select single item when onClick" in {
    //given
    val onSelect = mockFunction[Set[String], Unit]
    val props = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), onSelect = onSelect)
    val root = createTestRenderer(<(ListBox())(^.wrapped := props)()).root
    val items = root.children
    items.length shouldBe props.items.size
    val nextSelectIndex = 0

    //then
    onSelect.expects(Set(props.items(nextSelectIndex).id)).once()

    //when & then
    root.children(nextSelectIndex).props.onClick(literal(ctrlKey = false, metaKey = false))
    root.children(nextSelectIndex).props.className shouldBe {
      s"$listBoxItem $listBoxSelectedItem"
    }

    //when & then
    root.children(nextSelectIndex).props.onClick(literal(ctrlKey = false, metaKey = false))
    root.children(nextSelectIndex).props.className shouldBe {
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
    val root = createTestRenderer(<(ListBox())(^.wrapped := props)()).root
    val items = root.children
    items.length shouldBe props.items.size

    //then
    onSelect.expects(Set(props.items.head.id))
    onSelect.expects(Set(props.items.head.id, props.items(1).id))
    onSelect.expects(Set(props.items.head.id))
    onSelect.expects(Set.empty[String])

    //when & then
    root.children(0).props.onClick(literal(ctrlKey = true, metaKey = false))
    root.children(0).props.className shouldBe s"$listBoxItem $listBoxSelectedItem"

    //when & then
    root.children(1).props.onClick(literal(ctrlKey = false, metaKey = true))
    root.children(1).props.className shouldBe s"$listBoxItem $listBoxSelectedItem"
    
    //when & then
    root.children(1).props.onClick(literal(ctrlKey = false, metaKey = true))
    root.children(1).props.className shouldBe s"$listBoxItem "

    //when & then
    root.children(0).props.onClick(literal(ctrlKey = true, metaKey = false))
    root.children(0).props.className shouldBe s"$listBoxItem "
  }

  it should "reset selectedIds when update" in {
    //given
    val prevProps = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ))
    val renderer = createTestRenderer(<(ListBox())(^.wrapped := prevProps)())
    val root = renderer.root
    val items = root.children
    items.length shouldBe prevProps.items.size
    root.children(1).props.onClick(literal(ctrlKey = false, metaKey = false))
    root.children(0).props.className shouldBe s"$listBoxItem "
    root.children(1).props.className shouldBe s"$listBoxItem $listBoxSelectedItem"

    val props = prevProps.copy(selectedIds = Set("1"))

    //when
    TestRenderer.act { () =>
      renderer.update(<(ListBox())(^.wrapped := props)())
    }

    //then
    root.children(0).props.className shouldBe s"$listBoxItem $listBoxSelectedItem"
    root.children(1).props.className shouldBe s"$listBoxItem "
  }

  it should "render component" in {
    //given
    val props = ListBoxProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2", Some(ButtonImagesCss.accept))
    ))

    //when
    val result = createTestRenderer(<(ListBox())(^.wrapped := props)()).root

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
    val result = createTestRenderer(<(ListBox())(^.wrapped := props)()).root

    //then
    assertListBox(result, props)
  }

  private def assertListBox(result: TestInstance, props: ListBoxProps)(implicit pos: Position): Unit = {
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

    result.children.length shouldBe expectedItems.size
    result.children.toList.zip(expectedItems).foreach { case (item, expected) =>
      assertNativeComponent(item, expected)
    }
  }
}
