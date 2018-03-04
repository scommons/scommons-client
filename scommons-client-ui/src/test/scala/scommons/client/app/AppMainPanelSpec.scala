package scommons.client.app

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.TestSpec

class AppMainPanelSpec extends TestSpec {

  it should "render the component" in {
    //given
    val props = AppMainPanelProps(
      name = "test name",
      user = "test user",
      copyright = "test copyright",
      version = "test version"
    )
    val component = <(AppMainPanel.reactClass)(^.wrapped := props)(
      <.div()("Some child element 1"),
      <.div()("Some child element 2")
    )

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, <.div()(), { case List(header, cont, footer) =>
      assertComponent(header, AppHeader(), { headerProps: AppHeaderProps =>
        headerProps.name shouldBe props.name
        headerProps.user shouldBe props.user
      })
      assertDOMComponent(cont, <.div(^.className := "container-fluid")(
        <.div()("Some child element 1"),
        <.div()("Some child element 2")
      ))
      assertComponent(footer, AppFooter(), { footerProps: AppFooterProps =>
        footerProps.copyright shouldBe props.copyright
        footerProps.version shouldBe props.version
      })
    })
  }
}
