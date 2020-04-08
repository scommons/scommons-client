package scommons.client.showcase.table

import scommons.react._

object TablePanelDemo extends FunctionComponent[Unit] {

  protected def render(props: Props): ReactElement = {
    <.>()(
      <.h2()("TablePanel"),
      <.p()("Demonstrates table functionality"),

      <.h3()("Simple TablePanel"),
      <(SimpleTablePanel()).empty,
      
      <.h3()("TablePanel with custom cell renderer"),
      <(CustomTablePanel()).empty
    )
  }
}
