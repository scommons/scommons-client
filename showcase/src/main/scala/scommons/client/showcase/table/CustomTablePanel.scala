package scommons.client.showcase.table

import scommons.client.ui.table.TablePanelCss._
import scommons.client.ui.table._
import scommons.react._
import scommons.react.hooks._

object CustomTablePanel extends FunctionComponent[Unit] {

  case class CustomRowData(id: Int, status: String, link: String)

  lazy val tableComp = new TablePanel[Int, CustomRowData]

  protected def render(props: Props): ReactElement = {
    val (selectedIds, setSelectedIds) = useState(Set.empty[Int])

    val header = List(
      TableColumnData("Id"),
      TableColumnData("Status"),
      TableColumnData("")
    )

    val rows = List(
      CustomRowData(1, "info", "#"),
      CustomRowData(2, "warn", "#"),
      CustomRowData(3, "error", "#")
    )

    <(tableComp())(^.wrapped := new TablePanelProps[Int, CustomRowData](
      header = header,
      rows = rows,
      keyExtractor = _.id,
      rowClassExtractor = { (data, isSelected) =>
        val className =
          if (isSelected) tablePanelSelectedRow
          else tablePanelRow
        
        val addClass = data.status match {
          case "warn" => "warning"
          case "error" => "error"
          case _ => ""
        }
        
        s"$className $addClass"
      },
      cellRenderer = { (data, index) =>
        (index: @annotation.switch) match {
          case 0 => data.id.toString.asInstanceOf[ReactElement]
          case 1 => data.status.asInstanceOf[ReactElement]
          case 2 =>
            <.a(^.href := data.link)("details")
        }
      },
      selectedIds = selectedIds,
      onSelect = { data =>
        setSelectedIds(Set(data.id))
      }
    ))()
  }
}
