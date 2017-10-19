package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter

case class AppMainPanelProps(name: String = "App",
                             user: String = "user",
                             copyright: String = "copyright",
                             version: String = "version")

object AppMainPanel {

  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[AppMainPanelProps, Unit] { self =>
    val props = self.props.wrapped
    <.div()(
      <(AppHeader.reactClass)(^.wrapped := AppHeaderProps(props.name, props.user))(),
      <.div(^.className := "container-fluid")(
        self.props.children
      ),
      <(AppFooter.reactClass)(^.wrapped := AppFooterProps(props.copyright, props.version))()
    )
  }
}
