package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.tree.{BrowseTree, BrowseTreeProps}
import scommons.client.ui.{ButtonsPanel, ButtonsPanelProps}

case class AppBrowsePanelProps(buttonsProps: ButtonsPanelProps,
                               treeProps: BrowseTreeProps)

object AppBrowsePanel {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[AppBrowsePanelProps, Unit] { self =>
    val props = self.props.wrapped

    <.div(^.className := "row-fluid")(
      <.div(^.className := "span4")(
        <.div(^.className := "well sidebar-nav")(
          <.div(^.className := AppBrowsePanelCss.sidebarBp)(
            <(ButtonsPanel())(^.wrapped := props.buttonsProps)()
          ),
          <(BrowseTree())(^.wrapped := props.treeProps)()
        )
      ),
      <.div(^.className := "span8")(
        self.props.children
      )
    )
  }
}
