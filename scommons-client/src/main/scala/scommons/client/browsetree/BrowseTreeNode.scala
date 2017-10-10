package scommons.client.browsetree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.browsetree.BrowseTreeCss._

case class BrowseTreeNodeProps(text: String,
                               level: Int,
                               selected: Boolean = false,
                               open: Boolean = false)

object BrowseTreeNode {

  lazy val reactClass: ReactClass = React.createClass[BrowseTreeNodeProps, Unit] { self =>
    val props = self.props.wrapped
    val selectedClass = if (props.selected) browseTreeSelectedItem else ""
    val arrowClass = if (props.open) browseTreeOpenArrow else browseTreeClosedArrow
    val topItemClass = if (props.level == 0) browseTreeTopItem else ""
    val topItemImageClass = if (props.level == 0) browseTreeTopItemImageValue else ""

    val itemStyle =
      if (props.level == 0) Map.empty[String, String]
      else Map("padding-left" -> s"${props.level * 16}px")

    val childrenStyle =
      if (props.open) Map.empty[String, String]
      else Map("display" -> "none")

    <.div()(
      <.div(
        ^.className := s"$browseTreeItem $selectedClass $topItemClass",
        ^.style := itemStyle
      )(
        <.div(^.className := s"$browseTreeItem $browseTreeNode $topItemImageClass")(
          <.div(^.className := s"$browseTreeNodeIcon")(
            <.div(^.className := s"$arrowClass")()
          ),
          <.div(^.className := s"$browseTreeItemValue")(props.text)
        )
      ),
      <.div(^.style := childrenStyle)(
        self.props.children
      )
    )
  }
}
