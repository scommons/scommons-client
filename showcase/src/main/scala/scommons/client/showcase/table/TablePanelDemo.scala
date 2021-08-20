package scommons.client.showcase.table

import scommons.react._

object TablePanelDemo extends FunctionComponent[Unit] {

  private[table] var simpleTablePanel: UiComponent[Unit] = SimpleTablePanel
  private[table] var customTablePanel: UiComponent[Unit] = CustomTablePanel

  protected def render(props: Props): ReactElement = {
    <.>()(
      <.h2()("TablePanel"),
      <.p()("Demonstrates table functionality"),

      <.h3()("Simple TablePanel"),
      <(simpleTablePanel()).empty,
      
      <.h3()("TablePanel with custom cell renderer"),
      <(customTablePanel()).empty
    )
  }
}
