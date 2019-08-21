package scommons.client.showcase.demo

import scommons.client.ui.Buttons
import scommons.client.ui.tab._
import scommons.react._
import scommons.react.hooks._

object TabPanelDemo extends FunctionComponent[Unit] {

  private case class TabPanelDemoState(index1: Int = 0,
                                       index2: Int = 1,
                                       index3: Int = 2)
  
  protected def render(props: Props): ReactElement = {
    val (state, setState) = useStateUpdater(() => TabPanelDemoState())
    
    val items = List(
      TabItemData("First Tab", render = Some { _ =>
        <.div()("Content for first tab")
      }),
      TabItemData("Second Tab", image = Some(Buttons.FIND.image), component = Some(nestedTabs())),
      TabItemData("Third Tab", render = Some { _ =>
        <.div()("Content for third tab")
      })
    )

    <.>()(
      <.h2()("TabPanel"),
      <.p()("Demonstrates tabs functionality."),
      <.p()(
        <(TabPanel())(^.wrapped := TabPanelProps(
          items = items,
          selectedIndex = state.index1,
          onSelect = { (_, index) =>
            setState(s => s.copy(index1 = index))
          }
        ))()
      ),
      
      <.h3()("Tabs on the left"),
      <.p()(
        <(TabPanel())(^.wrapped := TabPanelProps(
          items = items,
          selectedIndex = state.index2,
          onSelect = { (_, index) =>
            setState(s => s.copy(index2 = index))
          },
          direction = TabDirection.Left
        ))()
      ),
      
      <.h3()("Tabs at the bottom"),
      <.p()(
        <(TabPanel())(^.wrapped := TabPanelProps(
          items = items,
          selectedIndex = state.index3,
          onSelect = { (_, index) =>
            setState(s => s.copy(index3 = index))
          },
          direction = TabDirection.Bottom
        ))()
      )
    )
  }

  private lazy val nestedItems = List(
    TabItemData("Nested Tab 1", image = Some(Buttons.ADD.image), render = Some { _ =>
      <.div()("Content for nested tab 1")
    }),
    TabItemData("Nested Tab 2", image = Some(Buttons.REMOVE.image), render = Some { _ =>
      <.div()("Content for nested tab 2")
    })
  )
  
  private lazy val nestedTabs = new FunctionComponent[Unit] {
    protected def render(props: Props): ReactElement = {
      val (selectedIndex, setSelectedIndex) = useState(0)
      
      <(TabPanel())(^.wrapped := TabPanelProps(
        items = nestedItems,
        selectedIndex = selectedIndex,
        onSelect = { (_, index) =>
          setSelectedIndex(index)
        }
      ))()
    }
  }
}
