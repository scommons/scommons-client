package scommons.client.app

import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class AppMainPanelSpec extends TestSpec with ShallowRendererUtils {

  it should "render the component" in {
    //given
    val props = AppMainPanelProps(
      name = "test name",
      user = "test user",
      copyright = "test copyright",
      version = "test version"
    )
    val component = <(AppMainPanel())(^.wrapped := props)(
      <.div()("Some child element 1"),
      <.div()("Some child element 2")
    )

    //when
    val result = shallowRender(component)

    //then
    assertNativeComponent(result, <.div()(), { case List(header, cont, footer) =>
      assertComponent(header, AppHeader) { case AppHeaderProps(name, user) =>
        name shouldBe props.name
        user shouldBe props.user
      }
      assertNativeComponent(cont, <.div(^.className := "container-fluid")(
        <.div()("Some child element 1"),
        <.div()("Some child element 2")
      ))
      assertComponent(footer, AppFooter) { case AppFooterProps(copyright, version) =>
        copyright shouldBe props.copyright
        version shouldBe props.version
      }
    })
  }
}
