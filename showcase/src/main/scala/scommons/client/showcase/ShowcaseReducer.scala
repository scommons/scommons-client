package scommons.client.showcase

import scommons.client.showcase.demo._
import scommons.client.showcase.table.TablePanelDemo
import scommons.client.ui.Buttons
import scommons.client.ui.tree._
import scommons.client.util.{ActionsData, BrowsePath}
import scommons.react.redux.task.{AbstractTask, TaskReducer}

case class ShowcaseState(currentTask: Option[AbstractTask])

object ShowcaseReducer {

  private val htmlItem = BrowseTreeItemData("HTML", BrowsePath("/html"), Some(Buttons.INFO.image),
    ActionsData.empty, Some(HTMLDemo()))

  private val buttonsItem = BrowseTreeItemData("Buttons", BrowsePath("/buttons"), Some(Buttons.CANCEL.image),
    ActionsData.empty, Some(ButtonsDemo()))

  private val treesItem = BrowseTreeItemData("Trees", BrowsePath("/trees"), Some(Buttons.OPEN.image),
    ActionsData.empty, Some(TreesDemo()))

  private val popupsItem = BrowseTreeItemData("Popups", BrowsePath("/popups"), Some(Buttons.INFO.image),
    ActionsData.empty, Some(PopupsDemo()))

  private val apiItem = BrowseTreeItemData("API", BrowsePath("/api"), Some(Buttons.SAVE.image),
    ActionsData.empty, Some(ApiDemoController()))

  private val tabPanelItem = BrowseTreeItemData("TabPanel", BrowsePath("/tab"), Some(Buttons.OK.image),
    ActionsData.empty, Some(TabPanelDemo()))

  private val tablePanelItem = BrowseTreeItemData("TablePanel", BrowsePath("/table"), Some(Buttons.ADD.image),
    ActionsData.empty, Some(TablePanelDemo()))

  private val paginationPanelItem = BrowseTreeItemData("PaginationPanel", BrowsePath("/pagination"),
    Some(Buttons.INFO.image), ActionsData.empty, Some(PaginationPanelDemo()))

  private val selectItem = BrowseTreeItemData("Select", BrowsePath("/select"), Some(Buttons.OK.image),
    ActionsData.empty, Some(SelectDemo()))

  private val listItem = BrowseTreeItemData("List", BrowsePath("/list"), Some(Buttons.ADD.image),
    ActionsData.empty, Some(ListDemo()))

  val widgetsNode = BrowseTreeNodeData("Widgets", BrowsePath("/widgets"), None, ActionsData.empty, None, List(
    htmlItem,
    buttonsItem,
    treesItem,
    popupsItem,
    tabPanelItem,
    tablePanelItem,
    paginationPanelItem,
    selectItem,
    listItem,
    apiItem
  ))

  def getTreeRoots(state: ShowcaseState): List[BrowseTreeData] = List(
    widgetsNode
  )

  def reduce(state: Option[ShowcaseState], action: Any): ShowcaseState = ShowcaseState(
    currentTask = TaskReducer(state.flatMap(_.currentTask), action)
  )
}
