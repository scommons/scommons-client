package scommons.client.ui.popup

import scommons.client.ui.{Buttons, ButtonsPanel, ButtonsPanelProps}
import scommons.client.util.ActionsData
import scommons.react.test._

class ModalFooterSpec extends TestSpec with TestRendererUtils {

  it should "render component with correct props" in {
    //given
    val props = ModalFooterProps(
      List(Buttons.OK, Buttons.CANCEL),
      ActionsData.empty
    )
    val component = <(ModalFooter())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertTestComponent(result, ButtonsPanel) {
      case ButtonsPanelProps(buttons, actions, dispatch, group, className) =>
        buttons shouldBe props.buttons
        actions shouldBe props.actions
        dispatch shouldBe props.dispatch
        group shouldBe false
        className shouldBe Some("modal-footer")
    }
  }
}
