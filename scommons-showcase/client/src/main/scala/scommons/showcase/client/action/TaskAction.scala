package scommons.showcase.client.action

import io.github.shogowada.scalajs.reactjs.redux.Action
import scommons.client.task.AbstractTask

trait TaskAction extends Action {

  def task: AbstractTask
}
