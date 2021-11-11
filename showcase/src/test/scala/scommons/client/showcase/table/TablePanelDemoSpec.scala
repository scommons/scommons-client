package scommons.client.showcase.table

import scommons.client.showcase.table.TablePanelDemo._
import scommons.react.test._

class TablePanelDemoSpec extends TestSpec with TestRendererUtils {

  TablePanelDemo.simpleTablePanel = mockUiComponent("SimpleTablePanel")
  TablePanelDemo.customTablePanel = mockUiComponent("CustomTablePanel")

  it should "render component" in {
    //given
    val component = <(TablePanelDemo())()()
    
    //when
    val result = createTestRenderer(component).root
    
    //then
    inside(result.children.toList) {
      case List(header1, p, header2, simpleTable, header3, customTable) =>
        assertNativeComponent(header1, <.h2()("TablePanel"))
        assertNativeComponent(p, <.p()("Demonstrates table functionality"))
        assertNativeComponent(header2, <.h3()("Simple TablePanel"))
        assertNativeComponent(simpleTable, <(simpleTablePanel()).empty)
        assertNativeComponent(header3, <.h3()("TablePanel with custom cell renderer"))
        assertNativeComponent(customTable, <(customTablePanel()).empty)
    }
  }
}
