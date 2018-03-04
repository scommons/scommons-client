package scommons.client.util

case class ActionsData(enabledCommands: Set[String],
                       onCommand: (Any => Any) => PartialFunction[String, Any],
                       focusedCommand: Option[String] = None)

object ActionsData {

  val empty = ActionsData(Set.empty[String], _ => PartialFunction.empty)
}
