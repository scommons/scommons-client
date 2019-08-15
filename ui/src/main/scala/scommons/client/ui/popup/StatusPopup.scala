package scommons.client.ui.popup

import scommons.react._

case class StatusPopupProps(text: String,
                            onHide: () => Unit)

object StatusPopup extends FunctionComponent[StatusPopupProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    <(Popup())(^.wrapped := PopupProps(
      onClose = () => (),
      focusable = false,
      overlayClass = "scommons-modal-no-overlay",
      popupClass = PopupCss.statusContent
    ))(
      <(WithAutoHide())(^.wrapped := WithAutoHideProps(props.onHide))(props.text)
    )
  }
}
