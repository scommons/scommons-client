package scommons.client.app

import scommons.react.test.TestSpec
import scommons.react.test.util.TestRendererUtils

class AppFooterSpec extends TestSpec with TestRendererUtils {

  it should "render the component" in {
    //given
    val props = AppFooterProps(
      "test copyright",
      "test version"
    )

    //when
    val result = testRender(<(AppFooter())(^.wrapped := props)())

    //then
    inside(result.children.toList) { case List(hr, footer) =>
      assertNativeComponent(hr, <.hr.empty)
      assertNativeComponent(footer,
        <.footer()(
          <.p(^.className := "text-center")(
            <.span()(props.copyright),
            " ",
            <.small()(props.version)
          )
        )
      )
    }
  }
}
