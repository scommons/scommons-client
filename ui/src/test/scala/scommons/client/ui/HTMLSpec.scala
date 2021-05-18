package scommons.client.ui

import scommons.react.test._

import scala.scalajs.js

class HTMLSpec extends TestSpec with TestRendererUtils {

  it should "render component with wordWrap = true" in {
    //given
    val props = HTMLProps("Test <p>html</p> <br/> text", wordWrap = true)

    //when
    val result = testRender(<(HTML())(^.wrapped := props)())

    //then
    result.`type` shouldBe "div"
    result.props.style shouldBe js.undefined
    result.props.dangerouslySetInnerHTML.__html shouldBe props.htmlText
  }

  it should "render component with wordWrap = false" in {
    //given
    val props = HTMLProps("Test <p>html</p> <br/> text", wordWrap = false)

    //when
    val result = testRender(<(HTML())(^.wrapped := props)())

    //then
    result.`type` shouldBe "div"
    result.props.style.whiteSpace shouldBe "nowrap"
    result.props.dangerouslySetInnerHTML.__html shouldBe props.htmlText
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
