package scommons.client.util

case class ActionsData(actionCommands: Set[String],
                       onCommand: String => Unit)

object ActionsData {

  val empty = ActionsData(Set.empty[String], _ => ())
}
