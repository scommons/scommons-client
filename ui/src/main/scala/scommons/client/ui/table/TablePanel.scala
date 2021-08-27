package scommons.client.ui.table

import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.table.TablePanelCss._
import scommons.react._
import scommons.react.dom._

case class TablePanelProps[K, T](header: List[TableColumnData],
                                 rows: Seq[T],
                                 keyExtractor: T => K,
                                 rowClassExtractor: (T, Boolean) => String,
                                 cellRenderer: (T, Int) => ReactElement,
                                 selectedIds: Set[K],
                                 onSelect: T => Unit)

object TablePanelProps {
  
  private lazy val _defaultRowClassExtractor = { (_: Any, isSelected: Boolean) =>
    if (isSelected) tablePanelSelectedRow
    else tablePanelRow
  }

  def defaultRowClassExtractor[T]: (T, Boolean) => String = {
    _defaultRowClassExtractor.asInstanceOf[(T, Boolean) => String]
  }

  def apply(header: List[TableColumnData],
            rows: List[TableRowData],
            selectedIds: Set[String] = Set.empty,
            onSelect: TableRowData => Unit = _ => ()): TablePanelProps[String, TableRowData] = {
    
    new TablePanelProps[String, TableRowData](
      header = header,
      rows = rows,
      keyExtractor = _.id,
      rowClassExtractor = defaultRowClassExtractor,
      cellRenderer = { (data, index) =>
        data.cells(index).asInstanceOf[ReactElement]
      },
      selectedIds = selectedIds,
      onSelect = onSelect
    )
  }
}

object TablePanel extends TablePanel[String, TableRowData]

class TablePanel[K, T] extends FunctionComponent[TablePanelProps[K, T]] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    val tableHeader = props.header.map { column =>
      <.th(^.colspan := 1)(column.title)
    }

    val tableBody = props.rows.map { data =>
      val key = props.keyExtractor(data)
      val rowClass = props.rowClassExtractor(data, isRowSelected(props, key))

      <.tr(
        ^.key := s"$key",
        ^.className := rowClass,
        ^.onClick := rowClick(props, key, data)
      )(props.header.indices.toList.map { cellIndex =>
        <.td(^.key := s"$cellIndex")(
          props.cellRenderer(data, cellIndex)
        )
      })
    }

    <.table(^.className := "table table-condensed", ^("cellSpacing") := "0")(
      <.thead(^("aria-hidden") := "false")(
        <.tr()(tableHeader)
      ),
      <.tbody()(tableBody)
    )
  }

  private def isRowSelected(props: TablePanelProps[K, T], key: K): Boolean = {
    props.selectedIds.contains(key)
  }

  private def rowClick(props: TablePanelProps[K, T], key: K, row: T): MouseSyntheticEvent => Unit = { _ =>
    if (!isRowSelected(props, key)) {
      //event.stopPropagation()

      props.onSelect(row)
    }
  }
}
