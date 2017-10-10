package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.browsetree._
import scommons.client.browsetree.BrowseTreeCss._

object BrowseTreePanel {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit] { _ =>
    <(BrowseTree.reactClass)()(
      <(BrowseTreeNode.reactClass)(^.wrapped := BrowseTreeNodeProps("Parent", level = 0, open = true))(
        <(BrowseTreeNode.reactClass)(^.wrapped := BrowseTreeNodeProps("Sub Parent 1", level = 1, selected = true, open = true))(
          <(BrowseTreeNode.reactClass)(^.wrapped := BrowseTreeNodeProps("Sub Parent 2", level = 2, open = true))(
            <(BrowseTreeItem.reactClass)(^.wrapped := BrowseTreeItemProps("Child 1", level = 3))(),
            <(BrowseTreeItem.reactClass)(^.wrapped := BrowseTreeItemProps("Child 2", level = 3, selected = true))()
          ),
          <(BrowseTreeItem.reactClass)(^.wrapped := BrowseTreeItemProps("Child 3", level = 2))()
        )
      ),
      <(BrowseTreeNode.reactClass)(^.wrapped := BrowseTreeNodeProps("Parent 2", level = 0))(),
      <(BrowseTreeItem.reactClass)(^.wrapped := BrowseTreeItemProps("Root Item", level = 0))(),
      <(BrowseTreeItem.reactClass)(^.wrapped := BrowseTreeItemProps("Root Selected Item", level = 0, selected = true))(),
      <.div()(
        <.div(^.className := s"$browseTreeSelectedItem $browseTreeTopItem $browseTreeItem")(
          <.div(^.className := s"$browseTreeNode $browseTreeTopItemImageValue $browseTreeItem")(
            <.div(^.className := s"$browseTreeNodeIcon")(
              <.div(^.className := s"$browseTreeOpenArrow")()
            ),
            <.div(^.className := s"$browseTreeItemValue")("Test Parent")
          )
        ),
        <.div(^.style := Map("padding-left" -> "16px"))(
          <.div(^.className := s"$browseTreeItem")(
            <.div(^.className := s"$browseTreeNode $browseTreeItem")(
              <.div(^.className := s"$browseTreeNodeIcon")(
                <.div(^.className := s"$browseTreeOpenArrow")()
              ),
              <.div(^.className := s"$browseTreeItemValue")("Test Child 1")
            )
          ),
          <.div(^.style := Map("padding-left" -> "16px"))(
            <.div(^.className := s"$browseTreeItem")(
              <.div(^.className := s"$browseTreeItem")(
                <.div(^.className := s"$browseTreeItemValue")("Test SubChild 1")
              )
            )
          )
        )
      ),
      <.div(^.className := s"$browseTreeSelectedItem $browseTreeTopItem")(
        <.div(^.className := s"$browseTreeItem $browseTreeTopItemImageValue")(
          <.div(^.className := s"$browseTreeItemValue")("Test Parent 2")
        )
      )
    )
  }
}
