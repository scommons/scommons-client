package scommons.client.ui.popup

import scommons.react._

case class LoadingPopupProps(show: Boolean)

object LoadingPopup extends FunctionComponent[LoadingPopupProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    <(Popup())(^.wrapped := PopupProps(
      show = props.show,
      onClose = () => (),
      closable = false,
      focusable = false,
      overlayClass = PopupCss.loadingOverlay,
      popupClass = PopupCss.loadingContent
    ))(
      <.img(^.className := PopupCss.loadingImg, ^.src := "")()
    )
  }
}
