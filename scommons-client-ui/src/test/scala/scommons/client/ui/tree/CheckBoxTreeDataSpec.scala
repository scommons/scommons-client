package scommons.client.ui.tree

import scommons.client.test.TestSpec
import scommons.client.ui.TriState

class CheckBoxTreeDataSpec extends TestSpec {

  it should "return flatten nodes list when flattenNodes" in {
    //given
    val item1 = CheckBoxTreeItemData("item1", TriState.Deselected, "/item1")
    val item2 = CheckBoxTreeItemData("item2", TriState.Deselected, "/item2")
    val item3 = CheckBoxTreeItemData("item3", TriState.Deselected, "/item3")
    val node1 = CheckBoxTreeNodeData("node1", TriState.Deselected, "/node1")
    val node2 = CheckBoxTreeNodeData("node2", TriState.Deselected, "/node2", children = List(item3))
    val node3 = CheckBoxTreeNodeData("node3", TriState.Deselected, "/node3", children = List(item2, node2))
    val roots = List(node1, node3, item1)

    //when
    val result = CheckBoxTreeData.flattenNodes(roots)

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
