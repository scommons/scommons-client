package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.TestSpec

class ModalBodySpec extends TestSpec {

  it should "render component with provided children" in {
    //given
    val component = <(ModalBody())()(
      <.p()("some children")
    )

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, <.div(^.className := "modal-body")(), { case List(child) =>
      assertDOMComponent(child, <.p()("some children"))
    })
  }
}
