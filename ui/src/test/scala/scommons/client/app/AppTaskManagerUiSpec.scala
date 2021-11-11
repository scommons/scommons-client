package scommons.client.app

import org.scalatest.Succeeded
import scommons.api.{ApiStatus, StatusResponse}
import scommons.client.app.AppTaskManagerUi._
import scommons.client.ui.popup._
import scommons.react.redux.task.TaskManagerUiProps
import scommons.react.test._

import scala.util.Success

class AppTaskManagerUiSpec extends TestSpec with TestRendererUtils {

  AppTaskManagerUi.statusPopup = mockUiComponent("StatusPopup")
  AppTaskManagerUi.loadingPopup = mockUiComponent("LoadingPopup")
  AppTaskManagerUi.errorPopup = mockUiComponent("ErrorPopup")

  it should "return error if unsuccessful response in errorHandler" in {
    //given
    val status = ApiStatus(500, "Some API error", "Some error details")
    val value = Success(StatusResponse(status))

    //when
    val (error, errorDetails) = AppTaskManagerUi.errorHandler(value)

    //then
    error shouldBe status.error
    errorDetails shouldBe status.details
  }

  it should "return None if successful response in errorHandler" in {
    //given
    val value = Success(StatusResponse(ApiStatus.Ok))

    //when
    val (error, errorDetails) = AppTaskManagerUi.errorHandler(value)

    //then
    error shouldBe None
    errorDetails shouldBe None
  }

  it should "call onHideStatus function when onHide status popup" in {
    //given
    val onHideStatus = mockFunction[Unit]
    val props = getTaskManagerUiProps(
      showLoading = true,
      status = Some("Fetching data"),
      onHideStatus = onHideStatus
    )
    val comp = createTestRenderer(<(AppTaskManagerUi())(^.wrapped := props)()).root
    val statusProps = findComponentProps(comp, statusPopup)

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
    val comp = createTestRenderer(<(AppTaskManagerUi())(^.wrapped := props)()).root
    val errorProps = findComponentProps(comp, errorPopup)

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
    val component = <(AppTaskManagerUi())(^.wrapped := props)()

    //when
    val result = createTestRenderer(component).root

    //then
    assertRenderingResult(result, props)
  }

  it should "render status" in {
    //given
    val props = getTaskManagerUiProps(
      status = Some("Fetching data")
    )
    val component = <(AppTaskManagerUi())(^.wrapped := props)()

    //when
    val result = createTestRenderer(component).root

    //then
    assertRenderingResult(result, props)
  }

  it should "render status and error" in {
    //given
    val props = getTaskManagerUiProps(
      status = Some("Fetching data"),
      error = Some("Some error")
    )
    val component = <(AppTaskManagerUi())(^.wrapped := props)()

    //when
    val result = createTestRenderer(component).root

    //then
    assertRenderingResult(result, props)
  }

  it should "render error with details" in {
    //given
    val props = getTaskManagerUiProps(
      error = Some("Some error"),
      errorDetails = Some("Some error details")
    )
    val component = <(AppTaskManagerUi())(^.wrapped := props)()

    //when
    val result = createTestRenderer(component).root

    //then
    assertRenderingResult(result, props)
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
  
  private def assertRenderingResult(result: TestInstance, props: TaskManagerUiProps): Unit = {
    val showStatus = props.status.isDefined
    val showError = props.error.isDefined
    
    val children = result.children.toList
    val (resStatusPopup, resLoadingPopup, resErrorPopup) = inside(children) {
      case List(sp) if showStatus => (Some(sp), None, None)
      case List(lp) if props.showLoading => (None, Some(lp), None)
      case List(sp, lp) if showStatus && props.showLoading => (Some(sp), Some(lp), None)
      case List(sp, ep) if showStatus && showError => (Some(sp), None, Some(ep))
      case List(ep) if showError => (None, None, Some(ep))
    }

    if (showStatus) {
      resStatusPopup should not be None
      assertTestComponent(resStatusPopup.get, statusPopup) { case StatusPopupProps(text, onHide) =>
        text shouldBe props.status.getOrElse("")
        onHide shouldBe props.onHideStatus
      }
    }
    
    if (props.showLoading) {
      resLoadingPopup should not be None
      assertTestComponent(resLoadingPopup.get, loadingPopup)(_ => Succeeded)
    }

    if (showError) {
      resErrorPopup should not be None
      assertTestComponent(resErrorPopup.get, errorPopup) { case ErrorPopupProps(error, onClose, details) =>
        error shouldBe props.error.getOrElse("")
        details shouldBe props.errorDetails
        onClose shouldBe props.onCloseErrorPopup
      }
    }
  }
}
