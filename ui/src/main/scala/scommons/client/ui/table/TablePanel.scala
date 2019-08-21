package scommons.client.ui.table

import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.table.TablePanelCss._
import scommons.react._

case class TablePanelProps(header: List[TableColumnData],
                           rows: List[TableRowData],
                           selectedIds: Set[String] = Set.empty,
                           onSelect: TableRowData => Unit = _ => ())

object TablePanel extends FunctionComponent[TablePanelProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    val tableHeader = props.header.map { column =>
      <.th(^.colspan := 1)(column.title)
    }

    val tableBody = props.rows.map { row =>
      val rowClass =
        if (isRowSelected(props, row)) tablePanelSelectedRow
        else tablePanelRow

      <.tr(
        ^.className := rowClass,
        ^.onClick := rowClick(props, row)
      )(row.cells.map { cell =>
        <.td()(cell)
      })
    }

    <.table(^.className := "table table-condensed", ^("cellSpacing") := "0")(
      <.thead(^("aria-hidden") := "false")(
        <.tr()(tableHeader)
      ),
      <.tbody()(tableBody)
    )
  }

  private def isRowSelected(props: TablePanelProps, row: TableRowData): Boolean = {
    props.selectedIds.contains(row.id)
  }

  private def rowClick(props: TablePanelProps, row: TableRowData): MouseSyntheticEvent => Unit = { _ =>
    if (!isRowSelected(props, row)) {
      //event.stopPropagation()

      props.onSelect(row)
    }
  }
}
