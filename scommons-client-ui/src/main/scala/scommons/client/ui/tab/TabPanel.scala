package scommons.client.ui.tab

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.{ImageLabelWrapper, UiComponent}

case class TabPanelProps(items: List[TabItemData],
                         selectedIndex: Int = 0,
                         onSelect: (TabItemData, Int) => Boolean = (_, _) => true,
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
              if (props.onSelect(item, index)) {
                self.setState(_.copy(selectedIndex = index))
              }
            }
          )(item.image match {
            case None => item.title
            case Some(image) => ImageLabelWrapper(image, Some(item.title), alignText = false)
          })
        )
      })

      val content = <.div(^.className := "tab-content")(itemWithIndexList.map { case (item, index) =>
        val activeClass =
          if (index == self.state.selectedIndex) "active"
          else ""

        <.div(^.className := s"tab-pane $activeClass")(
          item.component.map { comp =>
            <(comp)()()
          }.getOrElse(
            item.render.map { render =>
              render(self.props)
            }.getOrElse {
              <.div()()
            }
          )
        )
      })

      <.div(^.className := props.direction.style)(
        if (props.direction == TabDirection.Bottom) List(content, tabs)
        else List(tabs, content)
      )
    }
  )
}
