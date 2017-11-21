package scommons.client.util

case class ActionsData(enabledCommands: Set[String],
                       onCommand: String => Unit,
                       focusedCommand: Option[String] = None)

object ActionsData {

  val empty = ActionsData(Set.empty[String], _ => ())
}
