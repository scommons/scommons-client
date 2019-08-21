package scommons.client.ui.tab

import io.github.shogowada.statictags.Element
import org.scalajs.dom
import org.scalatest.Assertion
import scommons.client.ui.Buttons
import scommons.react._
import scommons.react.test.TestSpec
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.raw.ShallowInstance
import scommons.react.test.util.ShallowRendererUtils

class TabPanelSpec extends TestSpec
  with ShallowRendererUtils
  with TestDOMUtils {

  it should "call onSelect only once" in {
    //given
    val onSelect = mockFunction[TabItemData, Int, Unit]
    val items = List(
      TabItemData("Tab 1"),
      TabItemData("Tab 2")
    )
    val props = TabPanelProps(items, onSelect = onSelect)
    domRender(<(TabPanel())(^.wrapped := props)())
    val buttons = domContainer.querySelectorAll("a")
    buttons.length shouldBe items.size
    val tabs = domContainer.querySelectorAll("li")
    tabs.length shouldBe items.size
    tabs(props.selectedIndex).asInstanceOf[dom.Element].getAttribute("class") shouldBe "active"
    val panels = domContainer.querySelectorAll(".tab-pane")
    panels.length shouldBe items.size
    panels(props.selectedIndex).asInstanceOf[dom.Element].getAttribute("class") shouldBe "tab-pane active"
    val nextSelectIndex = 1

    //then
    onSelect.expects(items(nextSelectIndex), nextSelectIndex).once()

    //when click on new item
    fireDomEvent(Simulate.click(buttons(nextSelectIndex)))

    //when click on selected item
    fireDomEvent(Simulate.click(buttons(props.selectedIndex)))
  }

  it should "select tab with selectedIndex when update" in {
    //given
    val content1 = <.div()()
    val content2 = <.div()("Test second tab content")
    val content3 = <.div()("Test third tab content")
    val wrapper = new FunctionComponent[Unit] {
      protected def render(props: Props): ReactElement = {
        content2
      }
    }
    val items = List(
      TabItemData("Tab 1", image = Some(Buttons.ADD.image)),
      TabItemData("Tab 2", component = Some(wrapper())),
      TabItemData("Tab 3", render = Some(_ => content3))
    )
    val prevProps = TabPanelProps(items)
    val renderer = createRenderer()
    renderer.render(<(TabPanel())(^.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    assertTabPanel(comp, prevProps, List(content1, content2, content3))

    val props = TabPanelProps(items, selectedIndex = 1)

    //when
    renderer.render(<(TabPanel())(^.wrapped := props)())

    //then
    val compV2 = renderer.getRenderOutput()
    assertTabPanel(compV2, props, List(content1, content2, content3))
  }

  it should "render component" in {
    //given
    val content1 = <.div()()
    val content2 = <.div()("Test second tab content")
    val content3 = <.div()("Test third tab content")
    val wrapper = new FunctionComponent[Unit] {
      protected def render(props: Props): ReactElement = {
        content2
      }
    }
    val items = List(
      TabItemData("Tab 1", image = Some(Buttons.ADD.image)),
      TabItemData("Tab 2", component = Some(wrapper())),
      TabItemData("Tab 3", render = Some(_ => content3))
    )
    val props = TabPanelProps(items)
    val comp = <(TabPanel())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertTabPanel(result, props, List(content1, content2, content3))
  }

  it should "render component with pre-selected tab" in {
    //given
    val content1 = <.div()()
    val content2 = <.div()("Test second tab content")
    val content3 = <.div()("Test third tab content")
    val wrapper = new FunctionComponent[Unit] {
      protected def render(props: Props): ReactElement = {
        content2
      }
    }
    val items = List(
      TabItemData("Tab 1", image = Some(Buttons.ADD.image)),
      TabItemData("Tab 2", component = Some(wrapper())),
      TabItemData("Tab 3", render = Some(_ => content3))
    )
    val props = TabPanelProps(items, selectedIndex = 1)
    val comp = <(TabPanel())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertTabPanel(result, props, List(content1, content2, content3))
  }

  it should "render component with tabs at the bottom" in {
    //given
    val content1 = <.div()()
    val content2 = <.div()("Test second tab content")
    val content3 = <.div()("Test third tab content")
    val wrapper = new FunctionComponent[Unit] {
      protected def render(props: Props): ReactElement = {
        content2
      }
    }
    val items = List(
      TabItemData("Tab 1", image = Some(Buttons.ADD.image)),
      TabItemData("Tab 2", component = Some(wrapper())),
      TabItemData("Tab 3", render = Some(_ => content3))
    )
    val props = TabPanelProps(items, direction = TabDirection.Bottom)
    val comp = <(TabPanel())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertTabPanel(result, props, List(content1, content2, content3))
  }

  private def assertTabPanel(result: ShallowInstance,
                             props: TabPanelProps,
                             contentElements: List[Element]): Unit = {

    val itemWithIndexList = props.items.zipWithIndex

    val expectedTabs = itemWithIndexList.map { case (item, index) =>
      val maybeClassNameAttr =
        if (index == props.selectedIndex) Some(^.className := "active")
        else None

      <.li(maybeClassNameAttr, ^.key := s"$index")(
        <.a(^.href := "")(item.image match {
          case None => item.title
          case Some(image) => List(
            <.img(^.className := s"$image", ^.src := "")(),
            <.span(^.style := Map("paddingLeft" -> "3px"))(item.title)
          )
        })
      )
    }

    val expectedItems = itemWithIndexList.map { case (item, index) =>
      val activeClass =
        if (index == props.selectedIndex) "active"
        else ""

      <.div(^.className := s"tab-pane $activeClass", ^.key := s"$index")(
        item.component.map(compClass => <(compClass)()()).getOrElse {
          item.render.map(render => render(null)).getOrElse {
            <.div()()
          }
        }
      )
    }

    def assertTabsAndContent(tabs: ShallowInstance, content: ShallowInstance): Assertion = {
      assertNativeComponent(tabs, <.ul(^.className := "nav nav-tabs")(expectedTabs))
      assertNativeComponent(content, <.div(^.className := "tab-content")(expectedItems))
    }

    assertNativeComponent(result, <.div(^.className := props.direction.style)(), { case List(tabs, content) =>
      if (props.direction == TabDirection.Bottom) assertTabsAndContent(content, tabs)
      else assertTabsAndContent(tabs, content)
    })
  }
}
