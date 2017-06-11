package scommons.components.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

case class AppFooterProps(copyright: String = "copyright",
                          version: String = "version")

object AppFooter {

  lazy val reactClass = React.createClass[AppFooterProps, Unit]({ self =>
    val props = self.props.wrapped
    <.div()(
      <.hr()(),
      <.footer()(
        <.p(^.className := "text-center")(
          <.span()(props.copyright), //&nbsp;
          <.small()(props.version)
        )
      )
    )
  })
}
