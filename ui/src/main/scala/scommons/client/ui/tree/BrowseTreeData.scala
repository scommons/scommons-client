package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.util.{ActionsData, BrowsePath}

sealed trait BrowseTreeData {

  def text: String

  def path: BrowsePath

  def image: Option[String]

  def actions: ActionsData

  def reactClass: Option[ReactClass]
}

object BrowseTreeData {

  def flattenNodes(roots: List[BrowseTreeData]): List[BrowseTreeData] = {
    def loop(nodes: List[BrowseTreeData], result: List[BrowseTreeData]): List[BrowseTreeData] = nodes match {
      case Nil => result
      case head :: tail =>
        val newResult = head :: result

        loop(tail, head match {
          case node: BrowseTreeNodeData => loop(node.children, newResult)
          case _ => newResult
        })
    }

    loop(roots, Nil)
  }
}

case class BrowseTreeItemData(text: String,
                              path: BrowsePath,
                              image: Option[String] = None,
                              actions: ActionsData = ActionsData.empty,
                              reactClass: Option[ReactClass] = None) extends BrowseTreeData

case class BrowseTreeNodeData(text: String,
                              path: BrowsePath,
                              image: Option[String] = None,
                              actions: ActionsData = ActionsData.empty,
                              reactClass: Option[ReactClass] = None,
                              children: List[BrowseTreeData] = Nil) extends BrowseTreeData
