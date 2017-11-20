package scommons.client.ui.panel

import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestVirtualDOM._

class ModalBodySpec extends FlatSpec
  with Matchers
  with ShallowRendererUtils {

  "rendering" should "render component with provided children" in {
    //given
    val component = E(ModalBody())()(
      E.p()("some children")
    )

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, E.div(^.className := "modal-body")(), { case List(child) =>
      assertDOMComponent(child, E.p()("some children"))
    })
  }
}
