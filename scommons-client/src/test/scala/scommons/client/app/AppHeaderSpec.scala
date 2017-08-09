package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React._
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.raw.ReactTestUtils._

class AppHeaderSpec extends FlatSpec with Matchers {

  it should "render the component" in {
    //given
    val props = AppHeaderProps(
      "test app",
      "test user"
    )
    val component = createElement(AppHeader.reactClass, wrap(props))

    //when
    val result = renderIntoDocument(component)

    //then
    result.props.wrapped shouldBe props

    assertDOMElement(findReactElement(result),
      <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
          <div class="container-fluid">
            <button type="button" class="btn btn-navbar"
                    data-toggle="collapse" data-target=".nav-collapse">
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="brand" href="#">
              {s"${props.name}"}
            </a>
            <div class="nav-collapse collapse">
              <ul class="nav pull-right">
                <li class="dropdown">
                  <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <span>{s"${props.user}"}</span>
                    <b class="caret"/>
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    )
  }
}
