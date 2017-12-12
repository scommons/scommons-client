package scommons.client.task

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.scalatest.Assertion
import scommons.client.AsyncTestSpec
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.popup._

import scala.concurrent.Promise

class TaskManagerSpec extends AsyncTestSpec {

  it should "set status to None when onHideStatus" in {
    //given
    val task = FutureTask("Fetching data", Promise[Unit]().future)
    val props = TaskManagerProps(Some(task.key))
    val renderer = createRenderer()
    renderer.render(<(TaskManager())(^.wrapped := props)())
    val uiProps = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
    assertUiProps(uiProps, expected(showLoading = true, status = Some(s"${task.message}...")))

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
    assertUiProps(uiProps, expected(showLoading = true, status = Some(s"${task.message}...")))

    val e = new Exception("Test error")
    promise.failure(e)

    eventually {
      val uiPropsV2 = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
      uiPropsV2.showLoading shouldBe false
      uiPropsV2.error shouldBe Some(e.toString)
      uiPropsV2.errorDetails shouldBe Some(ErrorPopup.printStackTrace(e))

      //when
      uiProps.onCloseErrorPopup()

      //then
      val uiPropsV3 = findComponentProps(renderer.getRenderOutput(), TaskManagerUi)
      uiPropsV3.showLoading shouldBe false
      uiPropsV3.error shouldBe None
      uiPropsV3.errorDetails shouldBe None
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
      status = Some(s"${task.message}...")
    ))
  }

  private def assertRenderingResult(result: ComponentInstance, props: TaskManagerUiProps): Assertion = {
    assertComponent(result, TaskManagerUi(), { (uiProps: TaskManagerUiProps) =>
      assertUiProps(uiProps, props)
    })
  }

  private def assertUiProps(uiProps: TaskManagerUiProps,
                            expectedProps: TaskManagerUiProps): Assertion = {

    inside(uiProps) { case TaskManagerUiProps(showLoading, status, _, error, errorDetails, _) =>
      showLoading shouldBe expectedProps.showLoading
      status shouldBe expectedProps.status
      error shouldBe expectedProps.error
      errorDetails shouldBe expectedProps.errorDetails
    }
  }

  def expected(showLoading: Boolean = false,
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
