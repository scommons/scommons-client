package scommons.client.ui.popup

import scommons.client.test.TestSpec
import scommons.client.test.TestVirtualDOM._
import scommons.client.ui.{Buttons, ButtonsPanel, ButtonsPanelProps}
import scommons.client.util.ActionsData

class ModalFooterSpec extends TestSpec {

  it should "render component with correct props" in {
    //given
    val props = ModalFooterProps(
      List(Buttons.OK, Buttons.CANCEL),
      ActionsData.empty
    )
    val component = E(ModalFooter())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertComponent(result, ButtonsPanel(), { buttonsPanelProps: ButtonsPanelProps =>
      inside(buttonsPanelProps) { case ButtonsPanelProps(buttons, actions, group, className) =>
        buttons shouldBe props.buttons
        actions shouldBe props.actions
        group shouldBe false
        className shouldBe Some("modal-footer")
      }
    })
  }
}
