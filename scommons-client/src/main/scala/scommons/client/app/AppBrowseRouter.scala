package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import scommons.client.ui.tree.BrowseTreeData

case class AppBrowseRouterProps(treeRoots: List[BrowseTreeData],
                                component: ReactClass)

object AppBrowseRouter {

  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[AppBrowseRouterProps, Unit] { self =>
    val props = self.props.wrapped
    val allNodes = BrowseTreeData.flattenNodes(props.treeRoots)

    <.Switch()(
      (allNodes.map(_.path) :+ "/").map { path =>
        <.Route(^.path := path, ^.component := props.component)()
      }
    )
  }
}
