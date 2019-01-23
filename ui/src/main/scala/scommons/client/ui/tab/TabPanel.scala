package scommons.client.ui.tab

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.ImageLabelWrapper
import scommons.react.UiComponent

case class TabPanelProps(items: List[TabItemData],
                         selectedIndex: Int = 0,
                         onSelect: (TabItemData, Int) => Unit = (_, _) => (),
                         direction: TabDirection = TabDirection.Top) {

  require(selectedIndex >= 0 && selectedIndex < items.size,
    s"selectedIndex($selectedIndex) should be within items indices")
}

object TabPanel extends UiComponent[TabPanelProps] {

  private case class TabPanelState(selectedIndex: Int)

  protected def create(): ReactClass = React.createClass[PropsType, TabPanelState](
    getInitialState = { self =>
      TabPanelState(self.props.wrapped.selectedIndex)
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped.selectedIndex != props.selectedIndex) {
        self.setState(_.copy(selectedIndex = props.selectedIndex))
      }
    },
    render = { self =>
      val props = self.props.wrapped
      val itemWithIndexList = props.items.zipWithIndex

      val tabs = <.ul(^.className := "nav nav-tabs")(itemWithIndexList.map { case (item, index) =>
        <.li(
          ^.key := index.toString,
          if (index == self.state.selectedIndex) Some(^.className := "active")
          else None
        )(
          <.a(
            ^.href := "",
            ^.onClick := { e: MouseSyntheticEvent =>
              e.preventDefault()
              self.setState(_.copy(selectedIndex = index))
              props.onSelect(item, index)
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

        <.div(
          ^.key := index.toString,
          ^.className := s"tab-pane $activeClass"
        )(
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
