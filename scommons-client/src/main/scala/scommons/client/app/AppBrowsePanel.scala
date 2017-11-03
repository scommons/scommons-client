package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey
import scommons.client.ui.tree.{BrowseTree, BrowseTreeData, BrowseTreeProps}

case class BrowsePanelData(path: String, reactClass: ReactClass)

case class AppBrowsePanelProps(roots: List[BrowseTreeData],
                               routes: Map[BrowseTreeDataKey, BrowsePanelData],
                               selectedItem: Option[BrowseTreeDataKey])

object AppBrowsePanel extends RouterProps {

  def apply(): ReactClass = WithRouter(reactClass)

  private[app] lazy val reactClass = React.createClass[AppBrowsePanelProps, Unit] { self =>
    val props = self.props.wrapped

    def onSelectItem(data: BrowseTreeData): Unit = {
      props.routes.get(data.key).foreach { panel =>
        self.props.history.push(panel.path)
      }
    }

    val panelComp = props.selectedItem.flatMap(props.routes.get).map { (panel: BrowsePanelData) =>
      <(WithRouter(panel.reactClass))()()
    }

    <.div(^.className := "row-fluid")(
      <.div(^.className := "span4")(
        <.div(^.className := "well sidebar-nav")(
          <.div(^.className := AppBrowsePanelCss.sidebarBp)("ButtonsPanel"),
          <(BrowseTree())(^.wrapped := BrowseTreeProps(props.roots, props.selectedItem, onSelect = onSelectItem))()
        )
      ),
      <.div(^.className := "span8")(
        panelComp
      )
    )
  }
}
