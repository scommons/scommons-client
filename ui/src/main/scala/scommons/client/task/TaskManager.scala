package scommons.client.task

import scommons.api.ApiResponse
import scommons.client.ui.popup.ErrorPopup
import scommons.react._
import scommons.react.hooks._
import scommons.react.redux.task.AbstractTask

import scala.scalajs.js
import scala.util.{Failure, Success, Try}

case class TaskManagerProps(startTask: Option[AbstractTask])

/**
  * Displays status of running tasks.
  */
object TaskManager extends FunctionComponent[TaskManagerProps] {

  private[task] var uiComponent: UiComponent[TaskManagerUiProps] = TaskManagerUi
  
  private case class TaskManagerState(taskCount: Int = 0,
                                      status: Option[String] = None,
                                      error: Option[String] = None,
                                      errorDetails: Option[String] = None)

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped
    val (state, setState) = useStateUpdater(() => TaskManagerState())
    
    useEffect({ () =>
      props.startTask.foreach { task =>
        onTaskStart(setState, task)
      }
    }, List(props.startTask match {
      case None => js.undefined
      case Some(task) => task.asInstanceOf[js.Any]
    }))
    
    <(uiComponent())(^.wrapped := TaskManagerUiProps(
      showLoading = state.taskCount > 0,
      status = state.status,
      onHideStatus = { () =>
        setState(_.copy(status = None))
      },
      error = state.error,
      errorDetails = state.errorDetails,
      onCloseErrorPopup = { () =>
        setState(_.copy(error = None, errorDetails = None))
      }
    ))()
  }

  private def onTaskStart(setState: js.Function1[js.Function1[TaskManagerState, TaskManagerState], Unit],
                          task: AbstractTask): Unit = {

    task.onComplete { value: Try[_] =>
      onTaskFinish(setState, task, value)
    }

    setState(s => s.copy(
      taskCount = s.taskCount + 1,
      status = Some(s"${task.message}...")
    ))
  }

  private def onTaskFinish(setState: js.Function1[js.Function1[TaskManagerState, TaskManagerState], Unit],
                           task: AbstractTask,
                           value: Try[_]): Unit = {

    val durationMillis = System.currentTimeMillis() - task.startTime
    val statusMessage = s"${task.message}...Done ${formatDuration(durationMillis)} sec."

    val (error, errorDetails) = value match {
      case Success(result) =>
        result match {
          case res: ApiResponse if res.status.nonSuccessful =>
            (Some(res.status.error.getOrElse("Non-successful response")), res.status.details)
          case _ =>
            (None, None)
        }
      case Failure(e) => (Some(e.toString), Some(ErrorPopup.printStackTrace(e)))
    }

    setState(s => s.copy(
      taskCount = s.taskCount - 1,
      status = Some(statusMessage),
      error = error,
      errorDetails = errorDetails
    ))
  }

  private[task] def formatDuration(durationMillis: Long): String = {
    "%.3f".format(durationMillis / 1000.0)
  }
}
