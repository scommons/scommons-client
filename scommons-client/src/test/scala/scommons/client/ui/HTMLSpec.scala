package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.test.TestSpec
import scommons.client.test.TestUtils._

import scala.scalajs.js

class HTMLSpec extends TestSpec {

  it should "render component with wordWrap = true" in {
    //given
    val props = HTMLProps("Test <p>html</p> <br/> text", wordWrap = true)
    val component = <(HTML())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    result.`type` shouldBe "div"
    result.props.style shouldBe js.undefined
    result.props.dangerouslySetInnerHTML.__html shouldBe props.htmlText
  }

  it should "render component with wordWrap = false" in {
    //given
    val props = HTMLProps("Test <p>html</p> <br/> text", wordWrap = false)
    val component = <(HTML())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    result.`type` shouldBe "div"
    result.props.style.whiteSpace shouldBe "nowrap"
    result.props.dangerouslySetInnerHTML.__html shouldBe props.htmlText
  }

  it should "render component in the DOM" in {
    //given
    val props = HTMLProps("Test<p>html</p><br/>text", wordWrap = false)
    val component = <(HTML())(^.wrapped := props)()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div style="white-space: nowrap;">
        Test
        <p>html</p>
        <br/>
        text
      </div>
    )
  }

  it should "convert text to html when HTML.makeHtmlText" in {
    //when & then
    HTML.makeHtmlText("\n\n") shouldBe "<br/><br/>"
    HTML.makeHtmlText("\n\t") shouldBe "<br/>&nbsp&nbsp&nbsp&nbsp"
    HTML.makeHtmlText("\t") shouldBe "&nbsp&nbsp&nbsp&nbsp"
    HTML.makeHtmlText("\t\t") shouldBe "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
    HTML.makeHtmlText("a \n b \n c") shouldBe "a <br/> b <br/> c"
  }
}
