package scommons.client.showcase.table

import scommons.react._
import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class TablePanelDemoSpec extends TestSpec with ShallowRendererUtils {

  it should "render component" in {
    //given
    val component = <(TablePanelDemo())()()
    
    //when
    val result = shallowRender(component)
    
    //then
    assertNativeComponent(result,
      <.>()(
        <.h2()("TablePanel"),
        <.p()("Demonstrates table functionality"),

        <.h3()("Simple TablePanel"),
        <(SimpleTablePanel()).empty,

        <.h3()("TablePanel with custom cell renderer"),
        <(CustomTablePanel()).empty
      )
    )
  }
}
