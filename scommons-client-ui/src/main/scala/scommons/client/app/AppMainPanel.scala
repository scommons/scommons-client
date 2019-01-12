package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.react.UiComponent

case class AppMainPanelProps(name: String = "App",
                             user: String = "user",
                             copyright: String = "copyright",
                             version: String = "version")

object AppMainPanel extends UiComponent[AppMainPanelProps] {

  protected def create(): ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped
    <.div()(
      <(AppHeader())(^.wrapped := AppHeaderProps(props.name, props.user))(),
      <.div(^.className := "container-fluid")(
        self.props.children
      ),
      <(AppFooter())(^.wrapped := AppFooterProps(props.copyright, props.version))()
    )
  }
}
