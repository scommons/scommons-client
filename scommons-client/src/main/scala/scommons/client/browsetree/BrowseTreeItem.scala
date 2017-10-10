package scommons.client.browsetree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.browsetree.BrowseTreeCss._

case class BrowseTreeItemProps(text: String,
                               level: Int,
                               selected: Boolean = false)

object BrowseTreeItem {

  lazy val reactClass: ReactClass = React.createClass[BrowseTreeItemProps, Unit] { self =>
    val props = self.props.wrapped
    val selectedClass = if (props.selected) browseTreeSelectedItem else ""
    val topItemClass = if (props.level == 0) browseTreeTopItem else ""
    val topItemImageClass = if (props.level == 0) browseTreeTopItemImageValue else ""

    val itemStyle =
      if (props.level == 0) Map.empty[String, String]
      else Map("padding-left" -> s"${props.level * 16}px")

    <.div(
      ^.className := s"$browseTreeItem $selectedClass $topItemClass",
      ^.style := itemStyle
    )(
      <.div(^.className := s"$browseTreeItem $topItemImageClass")(
        <.div(^.className := s"$browseTreeItemValue")(props.text)
      )
    )
  }
}
