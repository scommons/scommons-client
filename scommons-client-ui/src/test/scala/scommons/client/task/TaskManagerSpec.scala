package scommons.client.task

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.scalatest.Assertion
import scommons.api.{ApiStatus, StatusResponse}
import scommons.client.test.AsyncTestSpec
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.popup._

import scala.concurrent.{Future, Promise}

class TaskManagerSpec extends AsyncTestSpec {

  it should "set status to None when onHideStatus" in {
    //given
    val task = FutureTask("Fetching data", Promise[Unit]().future)
    val props = TaskManagerProps(Some(task.key))
    val renderer = createRenderer()
    renderer.render(<(TaskManager())(^.wrapped := props)())
    val uiProps = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
    assertUiProps(uiProps, expected(showLoading = true, status = Some(s"${task.message}\\.\\.\\.")))

    //when
    uiProps.onHideStatus()

    //then
    val updatedUiProps = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
    assertUiProps(updatedUiProps, expected(showLoading = true, status = None))
  }

  it should "set error to None when onCloseErrorPopup" in {
    //given
    val promise = Promise[Unit]()
    val task = FutureTask("Fetching data", promise.future)
    val props = TaskManagerProps(Some(task.key))
    val renderer = createRenderer()
    renderer.render(<(TaskManager())(^.wrapped := props)())
    val uiProps = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
    assertUiProps(uiProps, expected(showLoading = true, status = Some(s"${task.message}\\.\\.\\.")))

    val e = new Exception("Test error")
    promise.failure(e)

    eventually {
      val uiPropsV2 = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
      assertUiProps(uiPropsV2, expected(
        showLoading = false,
        status = Some(s"${task.message}\\.\\.\\.Done \\d+\\.\\d+ sec\\."),
        error = Some(e.toString),
        errorDetails = Some(ErrorPopup.printStackTrace(e))
      ))

      //when
      uiPropsV2.onCloseErrorPopup()

      //then
      val uiPropsV3 = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
      assertUiProps(uiPropsV3, expected(
        showLoading = false,
        status = Some(s"${task.message}\\.\\.\\.Done \\d+\\.\\d+ sec\\."),
        error = None,
        errorDetails = None
      ))
    }
  }

  it should "render loading and status" in {
    //given
    val task = FutureTask("Fetching data", Promise[Unit]().future)
    val props = TaskManagerProps(Some(task.key))
    val component = <(TaskManager())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertRenderingResult(result, expected(
      showLoading = true,
      status = Some(s"${task.message}\\.\\.\\.")
    ))
  }

  it should "render error when exception" in {
    //given
    val promise = Promise[Unit]()
    val task = FutureTask("Fetching data", promise.future)
    val props = TaskManagerProps(Some(task.key))
    val renderer = createRenderer()
    renderer.render(<(TaskManager())(^.wrapped := props)())
    val uiProps = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
    assertUiProps(uiProps, expected(showLoading = true, status = Some(s"${task.message}...")))
    val e = new Exception("Test error")

    //when
    promise.failure(e)

    //then
    eventually {
      assertRenderingResult(renderer.getRenderOutput(), expected(
        showLoading = false,
        status = Some(s"${task.message}\\.\\.\\.Done \\d+\\.\\d+ sec\\."),
        error = Some(e.toString),
        errorDetails = Some(ErrorPopup.printStackTrace(e))
      ))
    }
  }

  it should "render error when unsuccessful response" in {
    //given
    val promise = Promise[StatusResponse]()
    val task = FutureTask("Fetching data", promise.future)
    val props = TaskManagerProps(Some(task.key))
    val renderer = createRenderer()
    renderer.render(<(TaskManager())(^.wrapped := props)())
    val uiProps = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
    assertUiProps(uiProps, expected(showLoading = true, status = Some(s"${task.message}...")))
    val resp = StatusResponse(ApiStatus(500, "Some API error", "Some error details"))

    //when
    promise.success(resp)

    //then
    eventually {
      assertRenderingResult(renderer.getRenderOutput(), expected(
        showLoading = false,
        status = Some(s"${task.message}\\.\\.\\.Done \\d+\\.\\d+ sec\\."),
        error = resp.status.error,
        errorDetails = resp.status.details
      ))
    }
  }

  it should "render status when task completed successfully" in {
    //given
    val promise = Promise[StatusResponse]()
    val task = FutureTask("Fetching data", promise.future)
    val props = TaskManagerProps(Some(task.key))
    val renderer = createRenderer()
    renderer.render(<(TaskManager())(^.wrapped := props)())
    val uiProps = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
    assertUiProps(uiProps, expected(showLoading = true, status = Some(s"${task.message}...")))
    val resp = StatusResponse.Ok

    //when
    promise.success(resp)

    //then
    eventually {
      assertRenderingResult(renderer.getRenderOutput(), expected(
        showLoading = false,
        status = Some(s"${task.message}\\.\\.\\.Done \\d+\\.\\d+ sec\\."),
        error = None,
        errorDetails = None
      ))
    }
  }

  it should "render status of already completed task" in {
    //given
    val task = FutureTask("Fetching data", Future.successful(()))
    val props = TaskManagerProps(Some(task.key))
    val renderer = createRenderer()
    renderer.render(<(TaskManager())(^.wrapped := props)())
    assertRenderingResult(renderer.getRenderOutput(), expected(
      showLoading = true,
      status = Some(s"${task.message}...")
    ))

    //when & then
    eventually {
      assertRenderingResult(renderer.getRenderOutput(), expected(
        showLoading = false,
        status = Some(s"${task.message}\\.\\.\\.Done \\d+\\.\\d+ sec\\."),
        error = None,
        errorDetails = None
      ))
    }
  }

  it should "render status of concurrent tasks" in {
    //given
    val renderer = createRenderer()
    val promise1 = Promise[StatusResponse]()
    val task1 = FutureTask("Fetching data 1", promise1.future)
    renderer.render(<(TaskManager())(^.wrapped := TaskManagerProps(Some(task1.key)))())
    assertRenderingResult(renderer.getRenderOutput(), expected(
      showLoading = true,
      status = Some(s"${task1.message}...")
    ))

    val promise2 = Promise[StatusResponse]()
    val task2 = FutureTask("Fetching data 2", promise2.future)
    renderer.render(<(TaskManager())(^.wrapped := TaskManagerProps(Some(task2.key)))())
    assertRenderingResult(renderer.getRenderOutput(), expected(
      showLoading = true,
      status = Some(s"${task2.message}...")
    ))

    //when
    promise1.success(StatusResponse.Ok)

    //then
    eventually {
      assertRenderingResult(renderer.getRenderOutput(), expected(
        showLoading = true,
        status = Some(s"${task1.message}\\.\\.\\.Done \\d+\\.\\d+ sec\\.")
      ))
    }.flatMap { _ =>
      //when
      promise2.success(StatusResponse.Ok)

      //then
      eventually {
        assertRenderingResult(renderer.getRenderOutput(), expected(
          showLoading = false,
          status = Some(s"${task2.message}\\.\\.\\.Done \\d+\\.\\d+ sec\\.")
        ))
      }
    }
  }

  it should "format duration in seconds properly" in {
    //when & then
    TaskManager.formatDuration(0L) shouldBe "0.000"
    TaskManager.formatDuration(3L) shouldBe "0.003"
    TaskManager.formatDuration(22L) shouldBe "0.022"
    TaskManager.formatDuration(33L) shouldBe "0.033"
    TaskManager.formatDuration(330L) shouldBe "0.330"
    TaskManager.formatDuration(333L) shouldBe "0.333"
    TaskManager.formatDuration(1132L) shouldBe "1.132"
    TaskManager.formatDuration(1333L) shouldBe "1.333"

    succeed
  }

  private def assertRenderingResult(result: ComponentInstance, props: TaskManagerUiProps): Assertion = {
    assertComponent(result, TaskManagerUi) { uiProps =>
      assertUiProps(uiProps, props)
    }
  }

  private def assertUiProps(uiProps: TaskManagerUiProps,
                            expectedProps: TaskManagerUiProps): Assertion = {

    inside(uiProps) { case TaskManagerUiProps(showLoading, status, _, error, errorDetails, _) =>
      showLoading shouldBe expectedProps.showLoading
      expectedProps.status match {
        case None => status shouldBe None
        case Some(statusRegex) => status.get should fullyMatch regex statusRegex
      }
      error shouldBe expectedProps.error
      errorDetails shouldBe expectedProps.errorDetails
    }
  }

  def expected(showLoading: Boolean,
               status: Option[String] = None,
               onHideStatus: () => Unit = () => (),
               error: Option[String] = None,
               errorDetails: Option[String] = None,
               onCloseErrorPopup: () => Unit = () => ()): TaskManagerUiProps = {

    TaskManagerUiProps(
      showLoading,
      status,
      onHideStatus,
      error,
      errorDetails,
      onCloseErrorPopup
    )
  }
}
