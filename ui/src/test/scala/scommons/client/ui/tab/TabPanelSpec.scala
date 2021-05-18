package scommons.client.ui.tab

import org.scalatest.Assertion
import scommons.client.ui.Buttons
import scommons.client.ui.tab.TabPanelSpec.MouseSyntheticEventMock
import scommons.react._
import scommons.react.test._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

class TabPanelSpec extends TestSpec with TestRendererUtils {

  it should "call onSelect only once" in {
    //given
    val onSelect = mockFunction[TabItemData, Int, Unit]
    val items = List(
      TabItemData("Tab 1"),
      TabItemData("Tab 2")
    )
    val props = TabPanelProps(items, onSelect = onSelect)
    val comp = testRender(<(TabPanel())(^.wrapped := props)())
    val buttons = findComponents(comp, <.a.name)
    buttons.length shouldBe items.size
    val tabs = findComponents(comp, <.li.name)
    tabs.length shouldBe items.size
    tabs(props.selectedIndex).props.className shouldBe "active"
    val panels = findComponents(comp, <.div.name).filter {
      case c if c.props.className.asInstanceOf[js.Any] == js.undefined => false
      case c => c.props.className.asInstanceOf[String].contains("tab-pane")
    }
    panels.length shouldBe items.size
    panels(props.selectedIndex).props.className shouldBe "tab-pane active"
    val nextSelectIndex = 1
    val event = mock[MouseSyntheticEventMock]
    (event.preventDefault _).expects().twice()

    //then
    onSelect.expects(items(nextSelectIndex), nextSelectIndex).once()

    //when click on new item
    buttons(nextSelectIndex).props.onClick(event.asInstanceOf[js.Any])

    //when click on selected item
    buttons(props.selectedIndex).props.onClick(event.asInstanceOf[js.Any])
  }

  it should "select tab with selectedIndex when update" in {
    //given
    val content1 = <.div()()
    val comp2 = "Comp2".asInstanceOf[ReactClass]
    val content2 = <(comp2)()()
    val content3 = <.div()("Test third tab content")
    val items = List(
      TabItemData("Tab 1", image = Some(Buttons.ADD.image)),
      TabItemData("Tab 2", component = Some(comp2)),
      TabItemData("Tab 3", render = Some(_ => content3))
    )
    val prevProps = TabPanelProps(items)
    val renderer = createTestRenderer(<(TabPanel())(^.wrapped := prevProps)())
    assertTabPanel(renderer.root.children(0), prevProps, List(content1, content2, content3))

    val props = TabPanelProps(items, selectedIndex = 1)

    //when
    TestRenderer.act { () =>
      renderer.update(<(TabPanel())(^.wrapped := props)())
    }

    //then
    assertTabPanel(renderer.root.children(0), props, List(content1, content2, content3))
  }

  it should "render component" in {
    //given
    val content1 = <.div()()
    val comp2 = "Comp2".asInstanceOf[ReactClass]
    val content2 = <(comp2)()()
    val content3 = <.div()("Test third tab content")
    val items = List(
      TabItemData("Tab 1", image = Some(Buttons.ADD.image)),
      TabItemData("Tab 2", component = Some(comp2)),
      TabItemData("Tab 3", render = Some(_ => content3))
    )
    val props = TabPanelProps(items)
    val comp = <(TabPanel())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertTabPanel(result, props, List(content1, content2, content3))
  }

  it should "render component with pre-selected tab" in {
    //given
    val content1 = <.div()()
    val comp2 = "Comp2".asInstanceOf[ReactClass]
    val content2 = <(comp2)()()
    val content3 = <.div()("Test third tab content")
    val items = List(
      TabItemData("Tab 1", image = Some(Buttons.ADD.image)),
      TabItemData("Tab 2", component = Some(comp2)),
      TabItemData("Tab 3", render = Some(_ => content3))
    )
    val props = TabPanelProps(items, selectedIndex = 1)
    val comp = <(TabPanel())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertTabPanel(result, props, List(content1, content2, content3))
  }

  it should "render component with tabs at the bottom" in {
    //given
    val content1 = <.div()()
    val comp2 = "Comp2".asInstanceOf[ReactClass]
    val content2 = <(comp2)()()
    val content3 = <.div()("Test third tab content")
    val items = List(
      TabItemData("Tab 1", image = Some(Buttons.ADD.image)),
      TabItemData("Tab 2", component = Some(comp2)),
      TabItemData("Tab 3", render = Some(_ => content3))
    )
    val props = TabPanelProps(items, direction = TabDirection.Bottom)
    val comp = <(TabPanel())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertTabPanel(result, props, List(content1, content2, content3))
  }

  private def assertTabPanel(result: TestInstance,
                             props: TabPanelProps,
                             contentElements: List[ReactElement]): Unit = {

    val expectedTabs = props.items.zipWithIndex.map { case (item, index) =>
      val maybeClassNameAttr =
        if (index == props.selectedIndex) Some(^.className := "active")
        else None

      <.li(maybeClassNameAttr)(
        <.a(^.href := "")(item.image match {
          case None => item.title
          case Some(image) => List(
            <.img(^.className := s"$image", ^.src := "")(),
            <.span(^.style := Map("paddingLeft" -> "3px"))(item.title)
          )
        })
      )
    }

    val expectedItems = contentElements.zipWithIndex.map { case (item, index) =>
      val activeClass =
        if (index == props.selectedIndex) "active"
        else ""

      <.div(^.className := s"tab-pane $activeClass")(item)
    }

    def assertTabsAndContent(tabs: TestInstance, content: TestInstance): Assertion = {
      assertNativeComponent(tabs, <.ul(^.className := "nav nav-tabs")(expectedTabs))
      assertNativeComponent(content, <.div(^.className := "tab-content")(expectedItems))
    }

    assertNativeComponent(result, <.div(^.className := props.direction.style)(), { case List(tabs, content) =>
      if (props.direction == TabDirection.Bottom) assertTabsAndContent(content, tabs)
      else assertTabsAndContent(tabs, content)
    })
  }
}

object TabPanelSpec {

  @JSExportAll
  trait MouseSyntheticEventMock {

    def preventDefault(): Unit
  }
}
