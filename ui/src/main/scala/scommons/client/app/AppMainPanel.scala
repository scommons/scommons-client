package scommons.client.app

import scommons.react._

case class AppMainPanelProps(name: String = "App",
                             user: String = "user",
                             copyright: String = "copyright",
                             version: String = "version")

object AppMainPanel extends FunctionComponent[AppMainPanelProps] {

  private[app] var appHeaderComp: UiComponent[AppHeaderProps] = AppHeader
  private[app] var appFooterComp: UiComponent[AppFooterProps] = AppFooter

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped
    
    <.>()(
      <(appHeaderComp())(^.wrapped := AppHeaderProps(props.name, props.user))(),
      <.div(^.className := "container-fluid")(
        compProps.children
      ),
      <(appFooterComp())(^.wrapped := AppFooterProps(props.copyright, props.version))()
    )
  }
}
