package scommons.client.task

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.api.ApiResponse
import scommons.client.ui.popup.ErrorPopup
import scommons.react.UiComponent
import scommons.react.redux.task.AbstractTask

import scala.util.{Failure, Success, Try}

case class TaskManagerProps(startTask: Option[AbstractTask])

/**
  * Controls running tasks.
  */
object TaskManager extends UiComponent[TaskManagerProps] {

  private case class TaskManagerState(taskCount: Int = 0,
                                      status: Option[String] = None,
                                      error: Option[String] = None,
                                      errorDetails: Option[String] = None)

  protected def create(): ReactClass = React.createClass[PropsType, TaskManagerState](
    getInitialState = { self =>
      self.props.wrapped.startTask.foldLeft(TaskManagerState()) { (currState, task) =>
        onTaskStart(self, currState, task)
      }
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped != props) {
        props.startTask.foreach { task =>
          self.setState(onTaskStart(self, self.state, task))
        }
      }
    },
    render = { self =>
      <(TaskManagerUi())(^.wrapped := TaskManagerUiProps(
        self.state.taskCount > 0,
        self.state.status,
        onHideStatus = { () =>
          self.setState(_.copy(status = None))
        },
        self.state.error,
        self.state.errorDetails,
        onCloseErrorPopup = { () =>
          self.setState(_.copy(error = None, errorDetails = None))
        }
      ))()
    }
  )

  private def onTaskStart(self: Self[TaskManagerProps, TaskManagerState],
                          currState: TaskManagerState,
                          task: AbstractTask): TaskManagerState = {

    task.onComplete { value: Try[_] =>
      onTaskFinish(self, task, value)
    }

    currState.copy(
      taskCount = currState.taskCount + 1,
      status = Some(s"${task.message}...")
    )
  }

  private def onTaskFinish(self: Self[TaskManagerProps, TaskManagerState],
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

    self.setState(_.copy(
      taskCount = self.state.taskCount - 1,
      status = Some(statusMessage),
      error = error,
      errorDetails = errorDetails
    ))
  }

  private[task] def formatDuration(durationMillis: Long): String = {
    "%.3f".format(durationMillis / 1000.0)
  }
}
