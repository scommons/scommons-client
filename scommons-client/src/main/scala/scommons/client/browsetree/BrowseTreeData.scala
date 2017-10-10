package scommons.client.browsetree

sealed trait BrowseTreeData

case class BrowseTreeItemData(text: String) extends BrowseTreeData

case class BrowseTreeNodeData(text: String, children: List[BrowseTreeData]) extends BrowseTreeData
