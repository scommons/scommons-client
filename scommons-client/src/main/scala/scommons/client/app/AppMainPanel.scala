package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

object AppMainPanel {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit] { self =>
    <.div()(
      <(AppHeader.reactClass)(^.wrapped := AppHeaderProps())(),
      <.div(^.className := "container-fluid")(
        self.props.children
      ),
      <(AppFooter.reactClass)(^.wrapped := AppFooterProps())()
    )
  }
}
