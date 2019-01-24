package scommons.client.showcase.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.table._

object TablePanelDemo {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit] { _ =>
    val header = List(
      TableColumnData("Id"),
      TableColumnData("Name"),
      TableColumnData("Description")
    )

    val rows = List(
      TableRowData("1", List(
        "1",
        "1 row",
        "1 test row"
      )),
      TableRowData("2", List(
        "2",
        "2 row",
        "2 test row"
      )),
      TableRowData("3", List(
        "3",
        "3 row",
        "3 test row"
      ))
    )

    <.div()(
      <.h2()("TablePanel"),
      <.p()("Demonstrates table functionality."),
      <.p()(
        <(TablePanel())(^.wrapped := TablePanelProps(header, rows))()
      )
    )
  }
}
