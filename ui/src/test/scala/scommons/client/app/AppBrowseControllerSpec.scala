package scommons.client.app

import scommons.client.app.AppBrowseController.findItemAndPath
import scommons.client.app.AppBrowseControllerSpec._
import scommons.client.test.raw.MemoryRouter._
import scommons.client.ui.Buttons
import scommons.client.ui.tree.{BrowseTreeItemData, BrowseTreeNodeData}
import scommons.client.util.{ActionsData, BrowsePath}
import scommons.react._
import scommons.react.redux.Dispatch
import scommons.react.test._

class AppBrowseControllerSpec extends TestSpec with TestRendererUtils {

  private val childItemPath = BrowsePath("/child-item")
  private val topNodePath = BrowsePath("/top-node", exact = false)
  private val openedTopNodePath = BrowsePath("/opened-top-node")

  private val buttons = List(Buttons.ADD, Buttons.REMOVE)

  private val childItem = BrowseTreeItemData("child item", childItemPath, None,
    ActionsData.empty.copy(enabledCommands = Set(Buttons.ADD.command)), Some(ChildItemComp()))

  private val topNode = BrowseTreeNodeData("top node", topNodePath, None,
    ActionsData.empty.copy(enabledCommands = Set(Buttons.REMOVE.command)), Some(TopNodeComp()), List(childItem))

  private val openedTopNode = BrowseTreeNodeData("initially opened top node", openedTopNodePath, None,
    ActionsData.empty.copy(enabledCommands = Set(Buttons.ADD.command)), None)

  private val dispatch: Dispatch = _ => ()
  private val treeRoots = List(topNode, openedTopNode)
  private val initiallyOpenedNodes = Set(openedTopNode.path)

  it should "render component with default (root) path" in {
    //given
    val component = createAppBrowseController(BrowsePath("/"))

    //when
    val result = testRender(component)

    //then
    assertRenderedProps(result, ActionsData.empty, None)
    findProps(result, ChildItemComp).length shouldBe 0
    findProps(result, TopNodeComp).length shouldBe 0
  }

  it should "render component with selected top node exact path" in {
    //given
    val component = createAppBrowseController(topNodePath)

    //when
    val result = testRender(component)

    //then
    assertRenderedProps(result, topNode.actions, Some(topNode.path))
    findProps(result, ChildItemComp).length shouldBe 0
    findProps(result, TopNodeComp).length should be > 0
  }

  it should "render component with selected top node non-exact path" in {
    //given
    val component = createAppBrowseController(topNodePath.copy(value = s"$topNodePath/1/2"))

    //when
    val result = testRender(component)

    //then
    assertRenderedProps(result, topNode.actions, Some(topNode.path))
    findProps(result, ChildItemComp).length shouldBe 0
    findProps(result, TopNodeComp).length should be > 0
  }

  it should "render component with not selected child when non-exact path" in {
    //given
    val component = createAppBrowseController(childItemPath.copy(value = s"$childItemPath/1/2"))

    //when
    val result = testRender(component)

    //then
    assertRenderedProps(result, ActionsData.empty, None)
    findProps(result, ChildItemComp).length shouldBe 0
    findProps(result, TopNodeComp).length shouldBe 0
  }

  it should "render component with selected child path" in {
    //given
    val component = createAppBrowseController(childItemPath)

    //when
    val result = testRender(component)

    //then
    assertRenderedProps(result, childItem.actions, Some(childItem.path), Set(topNode.path))
    findProps(result, ChildItemComp).length should be > 0
    findProps(result, TopNodeComp).length shouldBe 0
  }

  it should "render component with selected child path and children" in {
    //given
    val component = createAppBrowseController(childItemPath, Some(
      <(CustomChildrenComp()).empty
    ))

    //when
    val result = testRender(component)

    //then
    assertRenderedProps(result, childItem.actions, Some(childItem.path), Set(topNode.path))
    findProps(result, CustomChildrenComp).length shouldBe 1
    findProps(result, ChildItemComp).length should be > 0
    findProps(result, TopNodeComp).length shouldBe 0
  }

  it should "re-render component when selected item changes" in {
    //given
    val comp = testRender(createAppBrowseController(topNodePath))
    assertRenderedProps(comp, topNode.actions, Some(topNode.path))
    findProps(comp, TopNodeComp).length should be > 0
    val treeProps = findComponentProps(comp, AppBrowsePanel).treeProps

    //when
    treeProps.onSelect(childItem)

    //then
    assertRenderedProps(comp, childItem.actions, Some(childItem.path), Set(topNode.path))
    findProps(comp, ChildItemComp).length should be > 0
  }

  it should "return item path when findItemAndPath" in {
    //given
    val item1 = BrowseTreeItemData("item1", BrowsePath("/item1", exact = false))
    val item2 = BrowseTreeItemData("item2", BrowsePath("/item2/1", "/item2"))
    val item3 = BrowseTreeItemData("item3", BrowsePath("/item3"))
    val node1 = BrowseTreeNodeData("node1", BrowsePath("/node1"))
    val node2 = BrowseTreeNodeData("node2", BrowsePath("/node2"), children = List(item3))
    val node3 = BrowseTreeNodeData("node3", BrowsePath("/node3"), children = List(item2, node2))
    val roots = List(node1, node3, item1)

    //when & then
    findItemAndPath(roots, "/unknown") shouldBe None
    findItemAndPath(roots, "/item1/2") shouldBe Some(item1 -> Nil)
    findItemAndPath(roots, item1.path.value) shouldBe Some(item1 -> Nil)
    findItemAndPath(roots, "/item2") shouldBe Some(item2 -> List(node3))
    findItemAndPath(roots, item2.path.value) shouldBe Some(item2 -> List(node3))
    findItemAndPath(roots, item3.path.value) shouldBe Some(item3 -> List(node3, node2))
    findItemAndPath(roots, node1.path.value) shouldBe Some(node1 -> Nil)
    findItemAndPath(roots, node2.path.value) shouldBe Some(node2 -> List(node3))
    findItemAndPath(roots, node3.path.value) shouldBe Some(node3 -> Nil)
  }

  private def createAppBrowseController(targetPath: BrowsePath,
                                        children: Option[ReactElement] = None): ReactElement = {
    
    <.MemoryRouter(^.initialEntries := List(targetPath.value))(
      <(AppBrowseController())(^.wrapped := AppBrowseControllerProps(
        buttons,
        treeRoots,
        dispatch,
        initiallyOpenedNodes
      ))(
        children
      )
    )
  }

  private def assertRenderedProps(result: TestInstance,
                                  expectedActions: ActionsData,
                                  expectedSelectedItem: Option[BrowsePath],
                                  expectedOpenedNodes: Set[BrowsePath] = Set.empty): Unit = {

    val browsePanelProps = findComponentProps(result, AppBrowsePanel)
    val buttonsProps = browsePanelProps.buttonsProps
    buttonsProps.buttons shouldBe buttons
    buttonsProps.actions shouldBe expectedActions
    buttonsProps.dispatch shouldBe dispatch
    buttonsProps.group shouldBe true

    val treeProps = browsePanelProps.treeProps
    treeProps.roots shouldBe treeRoots
    treeProps.selectedItem shouldBe expectedSelectedItem
    treeProps.openedNodes shouldBe expectedOpenedNodes
    treeProps.initiallyOpenedNodes shouldBe initiallyOpenedNodes
  }
}

object AppBrowseControllerSpec {

  private object ChildItemComp extends FunctionComponent[Unit] {
    protected def render(props: Props): ReactElement = {
      <.p()("childItemComp")
    }
  }
  
  private object TopNodeComp extends FunctionComponent[Unit] {
    protected def render(props: Props): ReactElement = {
      <.p()("topNodeComp")
    }
  }

  private object CustomChildrenComp extends FunctionComponent[Unit] {
    protected def render(props: Props): ReactElement = {
      <.div()("customChildrenComp")
    }
  }
}
