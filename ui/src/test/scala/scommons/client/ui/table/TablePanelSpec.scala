package scommons.client.ui.table

import org.scalatest.Succeeded
import scommons.client.ui.table.TablePanelCss._
import scommons.react.test._

class TablePanelSpec extends TestSpec with TestRendererUtils {

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
    val comp = testRender(<(TablePanel())(^.wrapped := props)())
    val rows = findComponents(findComponents(comp, <.tbody.name).head, <.tr.name)
    rows.length shouldBe props.rows.size
    val nextSelectIndex = 1
    rows.head.props.className shouldBe tablePanelSelectedRow
    rows(nextSelectIndex).props.className shouldBe tablePanelRow

    //then
    onSelect.expects(props.rows(nextSelectIndex)).once()

    //when click on new row
    rows(nextSelectIndex).props.onClick(null)

    //when click on selected row
    rows.head.props.onClick(null)
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
    val renderer = createTestRenderer(<(TablePanel())(^.wrapped := prevProps)())
    assertTablePanel(renderer.root.children(0), prevProps)

    val props = prevProps.copy(selectedIds = Set("1"))

    //when
    TestRenderer.act { () =>
      renderer.update(<(TablePanel())(^.wrapped := props)())
    }

    //then
    assertTablePanel(renderer.root.children(0), props)
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
    val result = testRender(comp)

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
    val result = testRender(comp)

    //then
    assertTablePanel(result, props)
  }

  private def assertTablePanel(result: TestInstance, props: TablePanelProps[String, TableRowData]): Unit = {
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
    )(), inside(_) { case List(thead, tbody) =>
      assertNativeComponent(thead, <.thead(^("aria-hidden") := "false")(), inside(_) { case List(tr) =>
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
