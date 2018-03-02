package scommons.client.task

import io.github.shogowada.scalajs.reactjs.redux.Action

trait TaskAction extends Action {

  def task: AbstractTask
}
