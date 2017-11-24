package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.UiComponent

case class AppFooterProps(copyright: String = "copyright",
                          version: String = "version")

object AppFooter extends UiComponent[AppFooterProps] {

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped
    <.div()(
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
