package scommons.client.showcase.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.page._

object PaginationPanelDemo {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit] { _ =>
    <.div()(
      <.h2()("PaginationPanel"),
      <.p()("Demonstrates pagination functionality."),
      <.p()(
        <(PaginationPanel())(^.wrapped := PaginationPanelProps(5))()
      ),
      <.h3()("Pagination on the left"),
      <.p()(
        <(PaginationPanel())(^.wrapped := PaginationPanelProps(6, selectedPage = 3, alignment = PaginationAlignment.Left))()
      ),
      <.h3()("Pagination on the right"),
      <.p()(
        <(PaginationPanel())(^.wrapped := PaginationPanelProps(10, selectedPage = 10, alignment = PaginationAlignment.Right))()
      )
    )
  }
}
