package scommons.client.showcase.demo

import scommons.client.ui.table._
import scommons.react._
import scommons.react.hooks._

object TablePanelDemo extends FunctionComponent[Unit] {

  protected def render(props: Props): ReactElement = {
    val (selectedIds, setSelectedIds) = useState(Set.empty[String])
    
    val header = List(
      TableColumnData("Id"),
      TableColumnData("Name"),
      TableColumnData("Description")
    )

    val rows = List(
      TableRowData("1", List("1", "1 row", "1 test row")),
      TableRowData("2", List("2", "2 row", "2 test row")),
      TableRowData("3", List("3", "3 row", "3 test row"))
    )

    <.>()(
      <.h2()("TablePanel"),
      <.p()("Demonstrates table functionality."),
      <.p()(
        <(TablePanel())(^.wrapped := TablePanelProps(
          header = header,
          rows = rows,
          selectedIds = selectedIds,
          onSelect = { data =>
            setSelectedIds(Set(data.id))
          }
        ))()
      )
    )
  }
}
