package scommons.client.showcase.table

import scommons.client.showcase.table.CustomTablePanel._
import scommons.client.ui.table.TablePanelCss._
import scommons.client.ui.table._
import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class CustomTablePanelSpec extends TestSpec with ShallowRendererUtils {

  it should "select row when onSelect" in {
    //given
    val renderer = createRenderer()
    renderer.render(<(CustomTablePanel())()())
    val tableProps = findComponentProps(renderer.getRenderOutput(), tableComp)
    tableProps.selectedIds shouldBe Set.empty
    
    //when
    tableProps.onSelect(tableProps.rows(1))
    
    //then
    val updatedProps = findComponentProps(renderer.getRenderOutput(), tableComp)
    updatedProps.selectedIds shouldBe Set(2)
  }
  
  it should "render component" in {
    //given
    val component = <(CustomTablePanel())()()
    
    //when
    val result = shallowRender(component)
    
    //then
    assertComponent(result, tableComp) {
      case TablePanelProps(header, rows, keyExtractor, rowClassExtractor, cellRenderer, selectedIds, _) =>
        header shouldBe List(
          TableColumnData("Id"),
          TableColumnData("Status"),
          TableColumnData("")
        )
        rows shouldBe List(
          CustomRowData(1, "info", "#"),
          CustomRowData(2, "warn", "#"),
          CustomRowData(3, "error", "#")
        )
        keyExtractor(CustomRowData(123, "info", "#")) shouldBe 123
        
        rowClassExtractor(CustomRowData(1, "warn", "#"), false) shouldBe s"$tablePanelRow warning"
        
        cellRenderer(CustomRowData(2, "info", "#"), 1) shouldBe "info"
        
        selectedIds shouldBe Set.empty
    }
  }
}
