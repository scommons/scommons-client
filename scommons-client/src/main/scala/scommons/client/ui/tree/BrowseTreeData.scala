package scommons.client.ui.tree

import scommons.client.ui.tree.BrowseTreeData._
import scommons.client.util.Identity

sealed trait BrowseTreeData {

  lazy val key: BrowseTreeDataKey = Identity(this)
}

object BrowseTreeData {

  type BrowseTreeDataKey = Identity[BrowseTreeData]
}

case class BrowseTreeItemData(text: String,
                              image: Option[String]) extends BrowseTreeData

object BrowseTreeItemData {

  def apply(text: String): BrowseTreeItemData = BrowseTreeItemData(text, None)
}

case class BrowseTreeNodeData(text: String,
                              image: Option[String],
                              children: List[BrowseTreeData]) extends BrowseTreeData

object BrowseTreeNodeData {

  def apply(text: String, children: List[BrowseTreeData] = Nil): BrowseTreeNodeData =
    BrowseTreeNodeData(text, None, children)
}
