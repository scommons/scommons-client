package scommons.client.ui.popup

import scommons.react._

case class StatusPopupProps(text: String,
                            onHide: () => Unit)

object StatusPopup extends FunctionComponent[StatusPopupProps] {

  private[popup] var popupComp: UiComponent[PopupProps] = Popup
  private[popup] var withAutoHideComp: UiComponent[WithAutoHideProps] = WithAutoHide

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    <(popupComp())(^.wrapped := PopupProps(
      onClose = () => (),
      focusable = false,
      overlayClass = "scommons-modal-no-overlay",
      popupClass = PopupCss.statusContent
    ))(
      <(withAutoHideComp())(^.wrapped := WithAutoHideProps(props.onHide))(props.text)
    )
  }
}
