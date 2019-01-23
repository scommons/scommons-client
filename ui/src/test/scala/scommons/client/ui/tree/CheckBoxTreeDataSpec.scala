package scommons.client.ui.tree

import scommons.client.ui.TriState._
import scommons.react.test.TestSpec

class CheckBoxTreeDataSpec extends TestSpec {

  it should "return flatten nodes list when flattenNodes" in {
    //given
    val item1 = CheckBoxTreeItemData("item1", Deselected, "/item1")
    val item2 = CheckBoxTreeItemData("item2", Deselected, "/item2")
    val item3 = CheckBoxTreeItemData("item3", Deselected, "/item3")
    val node1 = CheckBoxTreeNodeData("node1", Deselected, "/node1")
    val node2 = CheckBoxTreeNodeData("node2", Deselected, "/node2", children = List(item3))
    val node3 = CheckBoxTreeNodeData("node3", Deselected, "/node3", children = List(item2, node2))
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
  
  it should "calculate correct node value when calcNodeValue" in {
    //given
    //
    // node1
    //   |
    // node3
    //   |-> item2
    //   |     |
    //   |-> node2
    //   |     |-> item3
    // item1
    //
    val item1 = CheckBoxTreeItemData("item1", Selected, "/item1")
    val item2 = CheckBoxTreeItemData("item2", Deselected, "/item2")
    val item3 = CheckBoxTreeItemData("item3", Selected, "/item3")
    val node1 = CheckBoxTreeNodeData("node1", Deselected, "/node1")
    val node2 = CheckBoxTreeNodeData("node2", Selected, "/node2", children = List(item3))
    val node3 = CheckBoxTreeNodeData("node3", Indeterminate, "/node3", children = List(item2, node2))
    val roots = List(node1, node3, item1)

    //when & then
    CheckBoxTreeData.calcNodeValue(roots, Deselected) shouldBe Indeterminate
    CheckBoxTreeData.calcNodeValue(node1.children, Deselected) shouldBe Deselected
    CheckBoxTreeData.calcNodeValue(node1.children, Selected) shouldBe Selected
    CheckBoxTreeData.calcNodeValue(node1.children, Indeterminate) shouldBe Indeterminate
    CheckBoxTreeData.calcNodeValue(node2.children, Deselected) shouldBe Selected
    CheckBoxTreeData.calcNodeValue(node3.children, Deselected) shouldBe Indeterminate
    CheckBoxTreeData.calcNodeValue(List(node3), Deselected) shouldBe Indeterminate
    CheckBoxTreeData.calcNodeValue(List(item1, item3, node2), Deselected) shouldBe Selected
    CheckBoxTreeData.calcNodeValue(List(item2, node1), Selected) shouldBe Deselected
  }
}
