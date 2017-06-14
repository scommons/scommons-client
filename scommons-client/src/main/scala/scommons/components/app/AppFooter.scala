package scommons.components.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

case class AppFooterProps(copyright: String = "copyright",
                          version: String = "version")

object AppFooter {

  lazy val reactClass: ReactClass = React.createClass[AppFooterProps, Unit] { self =>
    val props = self.props.wrapped
    <.div()(
      <.hr.empty,
      <.footer()(
        <.p(^.className := "text-center")(
          <.span()(props.copyright),
          "&nbsp;",
          <.small()(props.version)
        )
      )
    )
  }
}
