package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

object AppBrowsePanel {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit] { self =>
    <.div(^.className := "row-fluid")(
      <.div(^.className := "span4")(
        <.div(^.className := "well sidebar-nav")(
          <.div(^.className := AppBrowsePanelCss.sidebarBp)("ButtonsPanel"),
          self.props.children
        )
      ),
      <.div(^.className := "span8")()
    )
  }
}
