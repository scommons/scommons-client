package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import scommons.client.TestSpec
import scommons.client.test.raw.MemoryRouter._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.ui.Buttons
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey
import scommons.client.ui.tree.{BrowseTreeItemData, BrowseTreeNodeData}
import scommons.client.util.ActionsData

class AppBrowseControllerSpec extends TestSpec {

  private val buttons = List(Buttons.ADD, Buttons.REMOVE)
  private val childItem = BrowseTreeItemData("child item")
  private val topNode = BrowseTreeNodeData("top node", List(childItem))
  private val treeRoots = List(topNode)

  private val childItemComp = React.createClass[Unit, Unit] { _ =>
    <.p()("childItemComp")
  }
  private val topNodeComp = React.createClass[Unit, Unit] { _ =>
    <.p()("topNodeComp")
  }
  private val childItemPath = "/child-item"
  private val topNodePath = "/top-node"
  val routes = Map(
    childItem.key -> AppBrowseData(childItemPath, ActionsData(Set(Buttons.ADD.command), _ => ()), Some(childItemComp)),
    topNode.key -> AppBrowseData(topNodePath, ActionsData(Set(Buttons.REMOVE.command), _ => ()), Some(topNodeComp))
  )

  it should "render component with default (root) path" in {
    //given
    val component = createAppBrowseController("/")

    //when
    val result = renderIntoDocument(component)

    //then
    assertRenderedProps(result, ActionsData.empty, None)
    scryRenderedComponentsWithType(result, childItemComp).length shouldBe 0
    scryRenderedComponentsWithType(result, topNodeComp).length shouldBe 0
  }

  it should "render component with selected top node path" in {
    //given
    val component = createAppBrowseController(topNodePath)

    //when
    val result = renderIntoDocument(component)

    //then
    assertRenderedProps(result, routes(topNode.key).actions, Some(topNode.key))
    scryRenderedComponentsWithType(result, childItemComp).length shouldBe 0
    scryRenderedComponentsWithType(result, topNodeComp).length should be > 0
  }

  it should "render component with selected child path" in {
    //given
    val component = createAppBrowseController(childItemPath)

    //when
    val result = renderIntoDocument(component)

    //then
    assertRenderedProps(result, routes(childItem.key).actions, Some(childItem.key), Set(topNode.key))
    scryRenderedComponentsWithType(result, childItemComp).length should be > 0
    scryRenderedComponentsWithType(result, topNodeComp).length shouldBe 0
  }

  it should "re-render component when selected item changes" in {
    //given
    val comp = renderIntoDocument(createAppBrowseController(topNodePath))
    assertRenderedProps(comp, routes(topNode.key).actions, Some(topNode.key))
    scryRenderedComponentsWithType(comp, topNodeComp).length should be > 0
    val treeProps = findRenderedComponentProps(comp, AppBrowsePanel).treeProps

    //when
    treeProps.onSelect(childItem)

    //then
    assertRenderedProps(comp, routes(childItem.key).actions, Some(childItem.key), Set(topNode.key))
    scryRenderedComponentsWithType(comp, childItemComp).length should be > 0
  }

  it should "return item path when getItemPath" in {
    //given
    val item1 = BrowseTreeItemData("item1")
    val item2 = BrowseTreeItemData("item2")
    val item3 = BrowseTreeItemData("item3")
    val node1 = BrowseTreeNodeData("node1")
    val node2 = BrowseTreeNodeData("node2", List(item3))
    val node3 = BrowseTreeNodeData("node3", List(item2, node2))
    val roots = List(node1, node3, item1)

    //when
    val result = AppBrowseController.getItemPath(roots, item3)

    //then
    result shouldBe List(node3, node2)
  }

  private def createAppBrowseController(targetPath: String): ReactElement = {
    val props = AppBrowseControllerProps(treeRoots, routes, buttons)

    <.MemoryRouter(^.initialEntries := List(targetPath))(
      <.Switch()(
        (routes.values.map(_.path).toList :+ "/").map { path =>
          <.Route(^.path := path, ^.render := { _: Props[Unit] =>
            <(AppBrowseController())(^.wrapped := props)()
          })()
        }
      )
    )
  }

  private def assertRenderedProps(result: Instance,
                                  expectedActions: ActionsData,
                                  expectedSelectedItem: Option[BrowseTreeDataKey],
                                  expectedOpenedNodes: Set[BrowseTreeDataKey] = Set.empty[BrowseTreeDataKey]): Unit = {

    val browsePanelProps = findRenderedComponentProps(result, AppBrowsePanel)
    val buttonsProps = browsePanelProps.buttonsProps
    buttonsProps.buttons shouldBe buttons
    buttonsProps.actions shouldBe expectedActions
    buttonsProps.group shouldBe true

    val treeProps = browsePanelProps.treeProps
    treeProps.roots shouldBe treeRoots
    treeProps.selectedItem shouldBe expectedSelectedItem
    treeProps.openedNodes shouldBe expectedOpenedNodes
  }
}
