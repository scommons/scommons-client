package scommons.client.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import scommons.client.app.AppTaskManagerUi
import scommons.react.UiComponent
import scommons.react.redux.BaseStateController
import scommons.react.redux.task.{TaskManager, TaskManagerProps}

object ShowcaseTaskController extends BaseStateController[ShowcaseState, TaskManagerProps] {

  lazy val uiComponent: UiComponent[TaskManagerProps] = {
    TaskManager.uiComponent = AppTaskManagerUi
    TaskManager.errorHandler = AppTaskManagerUi.errorHandler
    TaskManager
  }

  def mapStateToProps(dispatch: Dispatch, state: ShowcaseState, props: Props[Unit]): TaskManagerProps = {
    TaskManagerProps(state.currentTask)
  }
}
