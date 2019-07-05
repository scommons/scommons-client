package scommons.client.task

import org.scalatest.Succeeded
import scommons.client.ui.popup._
import scommons.react._
import scommons.react.test.TestSpec
import scommons.react.test.raw.ShallowInstance
import scommons.react.test.util.ShallowRendererUtils

class TaskManagerUiSpec extends TestSpec with ShallowRendererUtils {

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

  private def assertRenderingResult(result: ShallowInstance, props: TaskManagerUiProps): Unit = {
    val showError = props.error.isDefined
    
    assertNativeComponent(result, <.>()(), { children =>
      val (statusPopup, loadingPopup, errorPopup) = children match {
        case List(sp) => (sp, None, None)
        case List(sp, lp) if props.showLoading => (sp, Some(lp), None)
        case List(sp, ep) if showError => (sp, None, Some(ep))
        case List(sp, lp, ep) => (sp, Some(lp), Some(ep))
      }
      
      assertComponent(statusPopup, StatusPopup) { case StatusPopupProps(text, show, onHide) =>
        show shouldBe props.status.isDefined
        text shouldBe props.status.getOrElse("")
        onHide shouldBe props.onHideStatus
      }
      
      if (props.showLoading) {
        loadingPopup should not be None
        assertComponent(loadingPopup.get, LoadingPopup) { case LoadingPopupProps(show) =>
          show shouldBe props.showLoading
        }
      }

      if (showError) {
        errorPopup should not be None
        assertComponent(errorPopup.get, ErrorPopup) { case ErrorPopupProps(show, error, onClose, details) =>
          show shouldBe showError
          error shouldBe props.error.getOrElse("")
          details shouldBe props.errorDetails
          onClose shouldBe props.onCloseErrorPopup
        }
      }
      
      Succeeded
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
