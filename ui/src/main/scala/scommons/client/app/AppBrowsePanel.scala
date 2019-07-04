package scommons.client.app

import scommons.client.ui.tree.{BrowseTree, BrowseTreeProps}
import scommons.client.ui.{ButtonsPanel, ButtonsPanelProps}
import scommons.react._

case class AppBrowsePanelProps(buttonsProps: ButtonsPanelProps,
                               treeProps: BrowseTreeProps)

object AppBrowsePanel extends FunctionComponent[AppBrowsePanelProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

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
        compProps.children
      )
    )
  }
}
