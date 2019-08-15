package scommons.client.ui.popup

import scommons.react._

object LoadingPopup extends FunctionComponent[Unit] {

  protected def render(compProps: Props): ReactElement = {
    <(Popup())(^.wrapped := PopupProps(
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
