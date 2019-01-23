package scommons.client.app

import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class AppFooterSpec extends TestSpec with ShallowRendererUtils {

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
    assertNativeComponent(result,
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
