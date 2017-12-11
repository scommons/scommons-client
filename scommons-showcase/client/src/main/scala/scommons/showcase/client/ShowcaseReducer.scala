package scommons.showcase.client

import scommons.client.app.AppBrowseData
import scommons.client.task.AbstractTask.AbstractTaskKey
import scommons.client.ui.Buttons
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey
import scommons.client.ui.tree.{BrowseTreeData, BrowseTreeItemData, BrowseTreeNodeData}
import scommons.client.util.ActionsData
import scommons.showcase.client.action.TaskAction
import scommons.showcase.client.demo._

case class ShowcaseState(roots: List[BrowseTreeData],
                         routes: Map[BrowseTreeDataKey, AppBrowseData],
                         currentTask: Option[AbstractTaskKey])

object ShowcaseReducer {

  private val repoItem = BrowseTreeItemData("Repo", Some(Buttons.REMOVE.image))
  private val reposItem = BrowseTreeNodeData("Repos", Some(Buttons.ADD.image), List(repoItem))
  private val htmlItem = BrowseTreeItemData("HTML", Some(Buttons.INFO.image))
  private val buttonsItem = BrowseTreeItemData("Buttons", Some(Buttons.CANCEL.image))
  private val popupsItem = BrowseTreeItemData("Popups", Some(Buttons.INFO.image))
  private val apiItem = BrowseTreeItemData("API", Some(Buttons.SAVE.image))
  private val widgetsNode = BrowseTreeNodeData("Widgets", List(
    htmlItem, buttonsItem, popupsItem, apiItem, reposItem
  ))

  private val defaultRoots = List(widgetsNode)

  val defaultRoutes = Map(
    widgetsNode.key -> AppBrowseData("/widgets", ActionsData.empty, None),
    htmlItem.key -> AppBrowseData("/html", ActionsData.empty, Some(HTMLDemo())),
    buttonsItem.key -> AppBrowseData("/buttons", ActionsData.empty, Some(ButtonsDemo())),
    popupsItem.key -> AppBrowseData("/popups", ActionsData.empty, Some(PopupsDemo())),
    apiItem.key -> AppBrowseData("/api", ActionsData.empty, Some(ApiDemoController())),
    reposItem.key -> AppBrowseData("/repos",
        ActionsData(Set(Buttons.REFRESH.command, Buttons.ADD.command), _ => ()), Some(ReposDemo())
    ),
    repoItem.key -> AppBrowseData("/repos/1",
      ActionsData(Set(Buttons.REMOVE.command), _ => ()), Some(ReposDemo())
    )
  )

  val reduce: (Option[ShowcaseState], Any) => ShowcaseState = (maybeState, action) =>
    ShowcaseState(
      roots = rootsReducer(maybeState.map(_.roots), action),
      routes = routesReducer(maybeState.map(_.routes), action),
      currentTask = currentTaskReducer(maybeState.flatMap(_.currentTask), action)
    )

  private def rootsReducer(roots: Option[List[BrowseTreeData]],
                           action: Any): List[BrowseTreeData] = action match {

    case _ => roots.getOrElse(defaultRoots)
  }

  private def routesReducer(routes: Option[Map[BrowseTreeDataKey, AppBrowseData]],
                            action: Any): Map[BrowseTreeDataKey, AppBrowseData] = action match {

    case _ => routes.getOrElse(defaultRoutes)
  }

  private def currentTaskReducer(currentTask: Option[AbstractTaskKey],
                                 action: Any): Option[AbstractTaskKey] = action match {

    case a: TaskAction => Some(a.task.key)
    case _ => None
  }
}
