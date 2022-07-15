package scommons.client.app

import scommons.client.ui.tree._
import scommons.client.ui.{Buttons, ButtonsPanel, ButtonsPanelProps}
import scommons.client.util.{ActionsData, BrowsePath}
import scommons.react.test._

class AppBrowsePanelSpec extends TestSpec with TestRendererUtils {

  it should "render the component" in {
    //given
    val b1 = Buttons.ADD
    val b2 = Buttons.REMOVE
    val buttonsPanelProps = ButtonsPanelProps(List(b1, b2), ActionsData.empty.copy(enabledCommands = Set(b1.command)))

    val childItem = BrowseTreeItemData("child item", BrowsePath("/child-item"))
    val topNode = BrowseTreeNodeData("top node", BrowsePath("/top-node"), children = List(childItem))
    val browseTreeProps = BrowseTreeProps(List(topNode))

    val props = AppBrowsePanelProps(buttonsPanelProps, browseTreeProps)
    val component = <(AppBrowsePanel())(^.wrapped := props)(
      <.div()("Some child element")
    )

    //when
    val result = testRender(component)

    //then
    assertNativeComponent(result, <.div(^.className := "row-fluid")(), inside(_) { case List(span4, span8) =>
      assertNativeComponent(span4, <.div(^.className := "span4")(), inside(_) { case List(sidebar) =>
        assertNativeComponent(sidebar, <.div(^.className := "well sidebar-nav")(), inside(_) { case List(sidebarBp, tree) =>
          assertNativeComponent(sidebarBp, <.div(^.className := AppBrowsePanelCss.sidebarBp)(), inside(_) { case List(bp) =>
            assertTestComponent(bp, ButtonsPanel) { bpProps =>
              bpProps shouldBe buttonsPanelProps
            }
          })
          assertTestComponent(tree, BrowseTree) { treeProps =>
            treeProps shouldBe browseTreeProps
          }
        })
      })
      assertNativeComponent(span8, <.div(^.className := "span8")(), inside(_) { case List(children) =>
        assertNativeComponent(children, <.div()("Some child element"))
      })
    })
  }
}
