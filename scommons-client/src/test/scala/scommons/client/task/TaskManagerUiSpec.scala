package scommons.client.task

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.TestSpec
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.popup._

class TaskManagerUiSpec extends TestSpec {

  it should "call onHideStatus function when onHide status popup" in {
    //given
    val onHideStatus = mockFunction[Unit]
    val props = getTaskManagerUiProps(
      showLoading = true,
      status = Some("Fetching data"),
      onHideStatus = onHideStatus
    )
    val comp = shallowRender(<(TaskManagerUi())(^.wrapped := props)())
    val statusProps = findComponentProps(comp, StatusPopup)

    //then
    onHideStatus.expects()

    //when
    statusProps.onHide()
  }

  it should "call onCloseErrorPopup function when onClose error popup" in {
    //given
    val onCloseErrorPopup = mockFunction[Unit]
    val props = getTaskManagerUiProps(
      error = Some("Some error"),
      onCloseErrorPopup = onCloseErrorPopup
    )
    val comp = shallowRender(<(TaskManagerUi())(^.wrapped := props)())
    val errorProps = findComponentProps(comp, ErrorPopup)

    //then
    onCloseErrorPopup.expects()

    //when
    errorProps.onClose()
  }

  it should "render loading and status" in {
    //given
    val props = getTaskManagerUiProps(
      showLoading = true,
      status = Some("Fetching data")
    )
    val component = <(TaskManagerUi())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertRenderingResult(result, props)
  }

  it should "render status" in {
    //given
    val props = getTaskManagerUiProps(
      status = Some("Fetching data")
    )
    val component = <(TaskManagerUi())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertRenderingResult(result, props)
  }

  it should "render status and error" in {
    //given
    val props = getTaskManagerUiProps(
      status = Some("Fetching data"),
      error = Some("Some error")
    )
    val component = <(TaskManagerUi())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertRenderingResult(result, props)
  }

  it should "render error with details" in {
    //given
    val props = getTaskManagerUiProps(
      error = Some("Some error"),
      errorDetails = Some("Some error details")
    )
    val component = <(TaskManagerUi())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertRenderingResult(result, props)
  }

  private def assertRenderingResult(result: ComponentInstance, props: TaskManagerUiProps): Unit = {
    assertDOMComponent(result, <.div()(), { case List(loadingPopup, statusPopup, errorPopup) =>
      assertComponent(loadingPopup, LoadingPopup(), { (loadingPopupProps: LoadingPopupProps) =>
        inside(loadingPopupProps) { case LoadingPopupProps(show) =>
          show shouldBe props.showLoading
        }
      })
      assertComponent(statusPopup, StatusPopup(), { (statusPopupProps: StatusPopupProps) =>
        inside(statusPopupProps) { case StatusPopupProps(text, show, onHide) =>
          show shouldBe props.status.isDefined
          text shouldBe props.status.getOrElse("")
          onHide shouldBe props.onHideStatus
        }
      })
      assertComponent(errorPopup, ErrorPopup(), { (errorPopupProps: ErrorPopupProps) =>
        inside(errorPopupProps) { case ErrorPopupProps(show, error, onClose, details) =>
          show shouldBe props.error.isDefined
          error shouldBe props.error.getOrElse("")
          details shouldBe props.errorDetails
          onClose shouldBe props.onCloseErrorPopup
        }
      })
    })
  }

  private def getTaskManagerUiProps(showLoading: Boolean = false,
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
