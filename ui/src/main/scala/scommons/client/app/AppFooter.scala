package scommons.client.app

import scommons.react._

case class AppFooterProps(copyright: String = "copyright",
                          version: String = "version")

object AppFooter extends FunctionComponent[AppFooterProps] {

  override protected def create(): ReactClass = {
    ReactMemo[Props](super.create(), { (prevProps, nextProps) =>
      prevProps.wrapped == nextProps.wrapped
    })
  }
  
  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped
    
    <.>()(
      <.hr.empty,
      <.footer()(
        <.p(^.className := "text-center")(
          <.span()(props.copyright),
          " ",
          <.small()(props.version)
        )
      )
    )
  }
}
