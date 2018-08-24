package scommons.client.ui.tree

import scommons.client.ui.TriState

sealed trait CheckBoxTreeData {

  def key: String
  
  def value: TriState

  def text: String

  def image: Option[String]
}

object CheckBoxTreeData {

  def flattenNodes(roots: List[CheckBoxTreeData]): List[CheckBoxTreeData] = {
    def loop(nodes: List[CheckBoxTreeData],
             result: List[CheckBoxTreeData]): List[CheckBoxTreeData] = nodes match {
      
      case Nil => result
      case head :: tail =>
        val newResult = head :: result

        loop(tail, head match {
          case node: CheckBoxTreeNodeData => loop(node.children, newResult)
          case _ => newResult
        })
    }

    loop(roots, Nil)
  }
}

case class CheckBoxTreeItemData(key: String,
                                value: TriState,
                                text: String,
                                image: Option[String] = None) extends CheckBoxTreeData

case class CheckBoxTreeNodeData(key: String,
                                value: TriState,
                                text: String,
                                image: Option[String] = None,
                                children: List[CheckBoxTreeData] = Nil) extends CheckBoxTreeData
