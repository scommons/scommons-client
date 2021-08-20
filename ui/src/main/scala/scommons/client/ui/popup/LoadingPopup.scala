package scommons.client.ui.popup

import scommons.client.ui.UiSettings
import scommons.react._

object LoadingPopup extends FunctionComponent[Unit] {

  private[popup] var popupComp: UiComponent[PopupProps] = Popup

  protected def render(compProps: Props): ReactElement = {
    <(popupComp())(^.wrapped := PopupProps(
      onClose = () => (),
      closable = false,
      focusable = false,
      overlayClass = PopupCss.loadingOverlay,
      popupClass = PopupCss.loadingContent
    ))(
      <.img(^.className := PopupCss.loadingImg, ^.src := UiSettings.imgClearCacheUrl)()
    )
  }
}
