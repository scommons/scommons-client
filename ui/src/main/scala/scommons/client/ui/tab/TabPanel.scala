package scommons.client.ui.tab

import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.ImageLabelWrapper
import scommons.react._
import scommons.react.dom._

case class TabPanelProps(items: List[TabItemData],
                         selectedIndex: Int = 0,
                         onSelect: (TabItemData, Int) => Unit = (_, _) => (),
                         direction: TabDirection = TabDirection.Top) {

  require(selectedIndex >= 0 && selectedIndex < items.size,
    s"selectedIndex($selectedIndex) should be within items indices")
}

object TabPanel extends FunctionComponent[TabPanelProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped
    val itemWithIndexList = props.items.zipWithIndex

    val tabs = <.ul(^.className := "nav nav-tabs")(itemWithIndexList.map { case (item, index) =>
      <.li(
        ^.key := index.toString,
        if (index == props.selectedIndex) Some(^.className := "active")
        else None
      )(
        <.a(
          ^.href := "",
          ^.onClick := { e: MouseSyntheticEvent =>
            e.preventDefault()
            if (index != props.selectedIndex) {
              props.onSelect(item, index)
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
        if (index == props.selectedIndex) "active"
        else ""

      <.div(
        ^.key := index.toString,
        ^.className := s"tab-pane $activeClass"
      )(
        item.component.map { comp =>
          <(comp)()()
        }.getOrElse(
          item.render.map { render =>
            render(compProps)
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
}
