package scommons.client.ui.table

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.UiComponent
import scommons.client.ui.table.TablePanelCss._

case class TablePanelProps(header: List[TableColumnData],
                           rows: List[TableRowData],
                           selectedIds: Set[String] = Set.empty,
                           onSelect: TableRowData => Unit = _ => ())

object TablePanel extends UiComponent[TablePanelProps] {

  private type TablePanelSelf = Self[PropsType, TablePanelState]

  private case class TablePanelState(selectedIds: Set[String])

  def apply(): ReactClass = reactClass
  lazy val reactClass: ReactClass = createComp

  private def createComp = React.createClass[PropsType, TablePanelState](
    getInitialState = { self =>
      TablePanelState(self.props.wrapped.selectedIds)
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped.selectedIds != props.selectedIds) {
        self.setState(_.copy(selectedIds = props.selectedIds))
      }
    },
    render = { self =>
      val props = self.props.wrapped

      val tableHeader = props.header.map { column =>
        <.th(^.colspan := 1)(column.title)
      }

      val tableBody = props.rows.map { row =>
        val rowClass =
          if (isRowSelected(self.state, row)) tablePanelSelectedRow
          else tablePanelRow

        <.tr(
          ^.className := rowClass,
          ^.onClick := rowClick(self, row)
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
  )

  private def isRowSelected(state: TablePanelState, row: TableRowData): Boolean = {
    state.selectedIds.contains(row.id)
  }

  private def rowClick(self: TablePanelSelf, row: TableRowData): MouseSyntheticEvent => Unit = { _ =>
    if (!isRowSelected(self.state, row)) {
      //event.stopPropagation()

      self.setState(s => s.copy(selectedIds = Set(row.id)))

      self.props.wrapped.onSelect(row)
    }
  }
}
