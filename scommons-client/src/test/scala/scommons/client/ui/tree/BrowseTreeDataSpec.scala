package scommons.client.ui.tree

import scommons.client.TestSpec
import scommons.client.util.BrowsePath

class BrowseTreeDataSpec extends TestSpec {

  it should "return flatten nodes list when flattenNodes" in {
    //given
    val item1 = BrowseTreeItemData("item1", BrowsePath("/item1"))
    val item2 = BrowseTreeItemData("item2", BrowsePath("/item2"))
    val item3 = BrowseTreeItemData("item3", BrowsePath("/item3"))
    val node1 = BrowseTreeNodeData("node1", BrowsePath("/node1"))
    val node2 = BrowseTreeNodeData("node2", BrowsePath("/node2"), children = List(item3))
    val node3 = BrowseTreeNodeData("node3", BrowsePath("/node3"), children = List(item2, node2))
    val roots = List(node1, node3, item1)

    //when
    val result = BrowseTreeData.flattenNodes(roots)

    //then
    result shouldBe List(
      item1,
      item3,
      node2,
      item2,
      node3,
      node1
    )
  }
}
