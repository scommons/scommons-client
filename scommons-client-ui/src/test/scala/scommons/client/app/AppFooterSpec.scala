package scommons.client.app

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.TestSpec

class AppFooterSpec extends TestSpec {

  it should "render the component" in {
    //given
    val props = AppFooterProps(
      "test copyright",
      "test version"
    )
    val component = <(AppFooter())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.div()(
        <.hr.empty,
        <.footer()(
          <.p(^.className := "text-center")(
            <.span()(props.copyright),
            " ",
            <.small()(props.version)
          )
        )
      )
    )
  }
}
