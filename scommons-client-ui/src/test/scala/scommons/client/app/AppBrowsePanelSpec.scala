package scommons.client.app

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.test.TestSpec
import scommons.client.ui.tree._
import scommons.client.ui.{Buttons, ButtonsPanel, ButtonsPanelProps}
import scommons.client.util.{ActionsData, BrowsePath}

class AppBrowsePanelSpec extends TestSpec {

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
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, <.div(^.className := "row-fluid")(), { case List(span4, span8) =>
      assertDOMComponent(span4, <.div(^.className := "span4")(), { case List(sidebar) =>
        assertDOMComponent(sidebar, <.div(^.className := "well sidebar-nav")(), { case List(sidebarBp, tree) =>
          assertDOMComponent(sidebarBp, <.div(^.className := AppBrowsePanelCss.sidebarBp)(), { case List(bp) =>
            assertComponent(bp, ButtonsPanel(), { bpProps: ButtonsPanelProps =>
              bpProps shouldBe buttonsPanelProps
            })
          })
          assertComponent(tree, BrowseTree(), { treeProps: BrowseTreeProps =>
            treeProps shouldBe browseTreeProps
          })
        })
      })
      assertDOMComponent(span8, <.div(^.className := "span8")(), { case List(children) =>
        assertDOMComponent(children, <.div()("Some child element"))
      })
    })
  }
}
