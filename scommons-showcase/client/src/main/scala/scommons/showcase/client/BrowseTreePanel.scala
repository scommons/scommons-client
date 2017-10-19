package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.browsetree._

object BrowseTreePanel {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit] { _ =>
    <(BrowseTree())(^.wrapped := BrowseTreeProps(List(
      BrowseTreeNodeData("Parent", List(
        BrowseTreeNodeData("Sub Parent 1", List(
          BrowseTreeNodeData("Sub Parent 2", List(
            BrowseTreeItemData("Child 1"),
            BrowseTreeItemData("Child 2")
          )),
          BrowseTreeItemData("Child 3")
        ))
      )),
      BrowseTreeNodeData("Parent 2", Nil),
      BrowseTreeItemData("Root Item")
    )))()
  }
}
