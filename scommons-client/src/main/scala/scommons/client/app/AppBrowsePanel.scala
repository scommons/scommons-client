package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import scommons.client.browsetree.BrowseTreeData.BrowseTreeDataKey
import scommons.client.browsetree.{BrowseTree, BrowseTreeData, BrowseTreeProps}

case class BrowsePanel(path: String, render: Props[_] => ReactElement)

case class AppBrowsePanelProps(roots: List[BrowseTreeData],
                               routes: Map[BrowseTreeDataKey, BrowsePanel])

object AppBrowsePanel extends RouterProps {

  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[AppBrowsePanelProps, Unit](
    getInitialState = { self =>
      println(s"App: location.pathname: ${self.props.location.pathname}")
      println(s"App: match.path: ${self.props.`match`.path}")
      ()
    },
    render = { self =>
      val props = self.props.wrapped

      val routes = props.routes.values.map { panel =>
        <.Route(^.path := panel.path, ^.render := panel.render)()
      }

      def onSelectItem(data: BrowseTreeData): Unit = {
        props.routes.get(data.key).foreach { panel =>
          self.props.history.push(panel.path)
        }
      }

      <.div(^.className := "row-fluid")(
        <.div(^.className := "span4")(
          <.div(^.className := "well sidebar-nav")(
            <.div(^.className := AppBrowsePanelCss.sidebarBp)("ButtonsPanel"),
            <(BrowseTree())(^.wrapped := BrowseTreeProps(props.roots, onSelect = onSelectItem))()
          )
        ),
        <.div(^.className := "span8")(
          <.Switch()(
            routes
          )
        )
      )
    }
  )
}
