package scommons.client.app

import scommons.react._

case class AppMainPanelProps(name: String = "App",
                             user: String = "user",
                             copyright: String = "copyright",
                             version: String = "version")

object AppMainPanel extends FunctionComponent[AppMainPanelProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped
    
    <.>()(
      <(AppHeader())(^.wrapped := AppHeaderProps(props.name, props.user))(),
      <.div(^.className := "container-fluid")(
        compProps.children
      ),
      <(AppFooter())(^.wrapped := AppFooterProps(props.copyright, props.version))()
    )
  }
}
