package scommons.client.app

import scommons.react.test.TestSpec
import scommons.react.test.util.TestRendererUtils

class AppHeaderSpec extends TestSpec with TestRendererUtils {

  it should "render the component" in {
    //given
    val props = AppHeaderProps(
      "test app",
      "test user"
    )

    //when
    val result = testRender(<(AppHeader())(^.wrapped := props)())

    //then
    assertNativeComponent(result.children(0),
      <.div(^.className := "navbar navbar-inverse navbar-fixed-top")(
        <.div(^.className := "navbar-inner")(
          <.div(^.className := "container-fluid")(
            <.button(
              ^.`type` := "button",
              ^.className := "btn btn-navbar",
              ^("data-toggle") := "collapse",
              ^("data-target") := ".nav-collapse"
            )(
              <.span(^.className := "icon-bar")(),
              <.span(^.className := "icon-bar")(),
              <.span(^.className := "icon-bar")()
            ),
            <.a(^.className := "brand", ^.href := "#")(
              s"${props.name}"
            ),
            <.div(^.className := "nav-collapse collapse")(
              <.ul(^.className := "nav pull-right")(
                <.li(^.className := "dropdown")(
                  <.a(
                    ^.className := "dropdown-toggle",
                    ^("data-toggle") := "dropdown",
                    ^.href := "#"
                  )(
                    <.span()(s"${props.user}"),
                    <.b(^.className := "caret")()
                  )
                )
              )
            )
          )
        )
      )
    )
  }
}
