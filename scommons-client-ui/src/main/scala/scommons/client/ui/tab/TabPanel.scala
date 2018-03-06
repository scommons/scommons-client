package scommons.client.ui.tab

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.UiComponent

case class TabPanelProps(items: List[TabItemData],
                         selectedIndex: Int = 0,
                         direction: TabDirection = TabDirection.Top)

object TabPanel extends UiComponent[TabPanelProps] {

  private case class TabPanelState(selectedIndex: Int)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, TabPanelState](
    getInitialState = { self =>
      TabPanelState(self.props.wrapped.selectedIndex)
    },
    render = { self =>
      val props = self.props.wrapped
      val itemWithIndexList = props.items.zipWithIndex

      val tabs = <.ul(^.className := "nav nav-tabs")(itemWithIndexList.map { case (item, index) =>
        val attributes =
          if (index == self.state.selectedIndex) Some(^.className := "active")
          else None

        <.li(attributes)(
          <.a(
            ^.href := "",
            ^.onClick := { e: MouseSyntheticEvent =>
              e.preventDefault()
              self.setState(_.copy(selectedIndex = index))
            }
          )(item.title)
        )
      })

      val content = <.div(^.className := "tab-content")(itemWithIndexList.map { case (item, index) =>
        val activeClass =
          if (index == self.state.selectedIndex) "active"
          else ""

        <.div(^.className := s"tab-pane $activeClass")(
          s"content for ${item.title}"
        )
      })

      <.div(^.className := props.direction.style)(
        if (props.direction == TabDirection.Bottom) List(content, tabs)
        else List(tabs, content)
      )
    }
  )
}
