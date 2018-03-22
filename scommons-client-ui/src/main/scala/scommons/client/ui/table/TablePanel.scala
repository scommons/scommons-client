package scommons.client.ui.table

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.UiComponent

case class TablePanelProps(header: List[TableColumnData],
                           rows: List[TableRowData],
                           onSelect: (TableRowData, Int) => Boolean = (_, _) => true)

object TablePanel extends UiComponent[TablePanelProps] {

  private case class TablePanelState(selectedIndex: Int)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, TablePanelState](
    getInitialState = { _ =>
      TablePanelState(0)
    },
//    componentWillReceiveProps = { (self, nextProps) =>
//      val props = nextProps.wrapped
//      if (self.props.wrapped != props) {
//        self.setState(_.copy(selectedIndex = props.selectedIndex))
//      }
//    },
    render = { self =>
      val props = self.props.wrapped

      val tableHeader = props.header.map { column =>
        <.th(^("colspan") := "1")(column.title)
      }

      val tableBody = props.rows.map { row =>
        <.tr()(row.cells.map { cell =>
          <.td()(cell)
        })
      }

      <.table(^.className := "table table-hover table-condensed", ^("cellspacing") := "0")(
        <.thead(^("aria-hidden") := "false")(
          <.tr()(tableHeader)
        ),
        <.tbody()(tableBody)
      )
    }
  )
}
