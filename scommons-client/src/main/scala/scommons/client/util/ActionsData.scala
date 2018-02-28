package scommons.client.util

case class ActionsData(enabledCommands: Set[String],
                       onCommand: PartialFunction[(String, Any => Any), Any],
                       focusedCommand: Option[String] = None)

object ActionsData {

  val empty = ActionsData(Set.empty[String], PartialFunction.empty)
}
