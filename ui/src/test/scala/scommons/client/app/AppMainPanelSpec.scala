package scommons.client.app

import scommons.client.app.AppMainPanel._
import scommons.react.test._

class AppMainPanelSpec extends TestSpec with TestRendererUtils {

  AppMainPanel.appHeaderComp = mockUiComponent("AppHeader")
  AppMainPanel.appFooterComp = mockUiComponent("AppFooter")

  it should "render the component" in {
    //given
    val props = AppMainPanelProps(
      name = "test name",
      user = "test user",
      copyright = "test copyright",
      version = "test version"
    )

    //when
    val result = createTestRenderer(<(AppMainPanel())(^.wrapped := props)(
      <.div()("Some child element 1"),
      <.div()("Some child element 2")
    )).root

    //then
    inside(result.children.toList) {
      case List(header, cont, footer) =>
        assertTestComponent(header, appHeaderComp) { case AppHeaderProps(name, user) =>
          name shouldBe props.name
          user shouldBe props.user
        }
        assertNativeComponent(cont, <.div(^.className := "container-fluid")(
          <.div()("Some child element 1"),
          <.div()("Some child element 2")
        ))
        assertTestComponent(footer, appFooterComp) { case AppFooterProps(copyright, version) =>
          copyright shouldBe props.copyright
          version shouldBe props.version
        }
    }
  }
}
