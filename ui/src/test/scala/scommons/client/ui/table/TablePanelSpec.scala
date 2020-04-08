package scommons.client.ui.table

import org.scalajs.dom
import org.scalatest.Succeeded
import scommons.client.ui.table.TablePanelCss._
import scommons.react.test.TestSpec
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.raw.ShallowInstance
import scommons.react.test.util.ShallowRendererUtils

class TablePanelSpec extends TestSpec
  with ShallowRendererUtils
  with TestDOMUtils {

  it should "call onSelect only once" in {
    //given
    val onSelect = mockFunction[TableRowData, Unit]
    val props = TablePanelProps(List(
      TableColumnData("Col1"),
      TableColumnData("Col2")
    ), List(
      TableRowData("1", List("Cell1.1", "Cell1.2")),
      TableRowData("2", List("Cell2.1", "Cell2.2"))
    ), selectedIds = Set("1"), onSelect = onSelect)
    domRender(<(TablePanel())(^.wrapped := props)())
    val rows = domContainer.querySelectorAll("tbody > tr")
    rows.length shouldBe props.rows.size
    val nextSelectIndex = 1
    rows(0).asInstanceOf[dom.Element].getAttribute("class") shouldBe tablePanelSelectedRow
    rows(nextSelectIndex).asInstanceOf[dom.Element].getAttribute("class") shouldBe tablePanelRow

    //then
    onSelect.expects(props.rows(nextSelectIndex)).once()

    //when click on new row
    fireDomEvent(Simulate.click(rows(nextSelectIndex)))

    //when click on selected row
    fireDomEvent(Simulate.click(rows(0)))
  }

  it should "select rows with selectedIds when update" in {
    //given
    val prevProps = TablePanelProps(List(
      TableColumnData("Col1"),
      TableColumnData("Col2")
    ), List(
      TableRowData("1", List("Cell1.1", "Cell1.2")),
      TableRowData("2", List("Cell2.1", "Cell2.2"))
    ))
    val renderer = createRenderer()
    renderer.render(<(TablePanel())(^.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    assertTablePanel(comp, prevProps)

    val props = prevProps.copy(selectedIds = Set("1"))

    //when
    renderer.render(<(TablePanel())(^.wrapped := props)())

    //then
    val compV2 = renderer.getRenderOutput()
    assertTablePanel(compV2, props)
  }

  it should "render component" in {
    //given
    val props = TablePanelProps(List(
      TableColumnData("Col1"),
      TableColumnData("Col2")
    ), List(
      TableRowData("1", List("Cell1.1", "Cell1.2")),
      TableRowData("2", List("Cell2.1", "Cell2.2"))
    ))
    val comp = <(TablePanel())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertTablePanel(result, props)
  }

  it should "render component with pre-selected row" in {
    //given
    val props = TablePanelProps(
      List(
        TableColumnData("Col1"),
        TableColumnData("Col2")
      ),
      List(
        TableRowData("1", List("Cell1.1", "Cell1.2")),
        TableRowData("2", List("Cell2.1", "Cell2.2"))
      ),
      selectedIds = Set("1")
    )
    val comp = <(TablePanel())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertTablePanel(result, props)
  }

  private def assertTablePanel(result: ShallowInstance, props: TablePanelProps[String, TableRowData]): Unit = {
    val expectedHeader = props.header.map { column =>
      <.th(^("colSpan") := "1")(column.title)
    }

    val expectedRows = props.rows.map { row =>
      val rowClass =
        if (props.selectedIds.contains(row.id)) tablePanelSelectedRow
        else tablePanelRow

      <.tr(^.key := row.id, ^.className := rowClass)() -> row.cells.zipWithIndex.map { case (cell, index) =>
        <.td(^.key := s"$index")(cell)
      }
    }

    assertNativeComponent(result, <.table(
      ^.className := "table table-condensed",
      ^("cellSpacing") := "0"
    )(), { case List(thead, tbody) =>
      assertNativeComponent(thead, <.thead(^("aria-hidden") := "false")(), { case List(tr) =>
        assertNativeComponent(tr, <.tr()(), { headerRow =>
          headerRow.size shouldBe expectedHeader.size
          headerRow.zip(expectedHeader).foreach { case (headerCell, expectedElem) =>
            assertNativeComponent(headerCell, expectedElem)
          }

          Succeeded
        })
      })
      assertNativeComponent(tbody, <.tbody()(), { rows =>
        rows.size shouldBe expectedRows.size
        rows.zip(expectedRows).foreach { case (resultRow, (expectedRowElem, expectedCells)) =>
          assertNativeComponent(resultRow, expectedRowElem, { resultCells =>
            resultCells.size shouldBe expectedCells.size
            resultCells.zip(expectedCells).foreach { case (resultCell, expectedCellElem) =>
              assertNativeComponent(resultCell, expectedCellElem)
            }

            Succeeded
          })
        }

        Succeeded
      })
    })
  }
}
