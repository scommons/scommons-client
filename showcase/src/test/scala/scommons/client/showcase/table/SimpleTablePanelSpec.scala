package scommons.client.showcase.table

import scommons.client.ui.table._
import scommons.react.test._

class SimpleTablePanelSpec extends TestSpec with TestRendererUtils {

  it should "select row when onSelect" in {
    //given
    val renderer = createTestRenderer(<(SimpleTablePanel())()())
    val tableProps = findComponentProps(renderer.root, TablePanel)
    tableProps.selectedIds shouldBe Set.empty
    
    //when
    tableProps.onSelect(tableProps.rows(1))
    
    //then
    val updatedProps = findComponentProps(renderer.root, TablePanel)
    updatedProps.selectedIds shouldBe Set("2")
  }
  
  it should "render component" in {
    //given
    val component = <(SimpleTablePanel())()()
    
    //when
    val result = testRender(component)
    
    //then
    assertTestComponent(result, TablePanel) {
      case TablePanelProps(header, rows, keyExtractor, rowClassExtractor, cellRenderer, selectedIds, _) =>
        header shouldBe List(
          TableColumnData("Id"),
          TableColumnData("Name"),
          TableColumnData("Description")
        )
        rows shouldBe List(
          TableRowData("1", List("1", "1 row", "1 test row")),
          TableRowData("2", List("2", "2 row", "2 test row")),
          TableRowData("3", List("3", "3 row", "3 test row"))
        )
        keyExtractor(TableRowData("123", Nil)) shouldBe "123"
        rowClassExtractor shouldBe TablePanelProps.defaultRowClassExtractor
        cellRenderer(TableRowData("123", List("test")), 0) shouldBe "test"
        selectedIds shouldBe Set.empty
    }
  }
}
