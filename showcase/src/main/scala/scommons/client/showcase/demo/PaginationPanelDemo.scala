package scommons.client.showcase.demo

import scommons.client.ui.page._
import scommons.react._

object PaginationPanelDemo extends FunctionComponent[Unit] {

  protected def render(props: Props): ReactElement = {
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
