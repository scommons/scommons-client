package scommons.client.task

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.api.ApiResponse
import scommons.client.task.AbstractTask.AbstractTaskKey
import scommons.client.ui.popup.ErrorPopup

import scala.util.{Failure, Success, Try}

case class TaskManagerProps(startTask: Option[AbstractTaskKey])

/**
  * Controls running tasks.
  */
object TaskManager {

  private case class TaskManagerState(taskCount: Int = 0,
                                      statusMessage: String = "",
                                      showError: Boolean = false,
                                      error: String = "",
                                      errorDetails: Option[String] = None)

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[TaskManagerProps, TaskManagerState](
    getInitialState = { self =>
      self.props.wrapped.startTask.map { taskKey =>
        onTaskStart(self, taskKey.obj)

        TaskManagerState(1, taskKey.obj.message + "...")
      }.getOrElse {
        TaskManagerState()
      }
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped != props) {
        props.startTask.foreach { taskKey =>
          val currState = self.state
          val statusMessage =
            if (currState.taskCount == 0) taskKey.obj.message + "..."
            else currState.statusMessage

          onTaskStart(self, taskKey.obj)
          self.setState(_.copy(taskCount = currState.taskCount + 1, statusMessage = statusMessage))
        }
      }
    },
    render = { self =>
      <(TaskManagerUi())(^.wrapped := TaskManagerUiProps(
        self.state.taskCount > 0,
        self.state.statusMessage,
        self.state.showError,
        self.state.error,
        self.state.errorDetails,
        onCloseErrorPopup = { () =>
          self.setState(_.copy(showError = false, error = "", errorDetails = None))
        }
      ))()
    }
  )

  private def onTaskStart(self: Self[TaskManagerProps, TaskManagerState], task: AbstractTask): Unit = {
    task.onComplete { value: Try[_] =>
      onTaskFinish(self, task, value)
    }
  }

  private def onTaskFinish(self: Self[TaskManagerProps, TaskManagerState],
                           task: AbstractTask,
                           value: Try[_]): Unit = {

    val durationMillis = System.currentTimeMillis() - task.startTime
    val currState = self.state
    val statusMessage =
      if (currState.taskCount == 1) task.message + s"...Done ${(durationMillis/10.0)/100} sec."
      else currState.statusMessage

    val (showError, error, errorDetails) = value match {
      case Success(result) =>
        result match {
          case res: ApiResponse if res.status.nonSuccessful =>
            (true, res.status.error.getOrElse("Non-successful response"), res.status.details)
          case _ =>
            (false, "", None)
        }
      case Failure(e) => (true, e.toString, Some(ErrorPopup.printStackTrace(e)))
    }

    self.setState(_.copy(
      taskCount = currState.taskCount - 1,
      statusMessage = statusMessage,
      showError = showError,
      error = error,
      errorDetails = errorDetails
    ))
  }
}
