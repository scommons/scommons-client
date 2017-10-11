package scommons.client.browsetree

import scommons.client.browsetree.BrowseTreeData._
import scommons.client.util.Identity

sealed trait BrowseTreeData {

  lazy val key: BrowseTreeDataKey = Identity(this)
}

object BrowseTreeData {

  type BrowseTreeDataKey = Identity[BrowseTreeData]
}

case class BrowseTreeItemData(text: String) extends BrowseTreeData

case class BrowseTreeNodeData(text: String, children: List[BrowseTreeData]) extends BrowseTreeData
