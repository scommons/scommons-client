package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes
import io.github.shogowada.statictags.{Attribute, AttributeSpec}
import scommons.react._

import scala.scalajs.js

case class HTMLProps(htmlText: String, wordWrap: Boolean)

object HTML extends FunctionComponent[HTMLProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    <.div(
      if (props.wordWrap) None
      else Some(^.style := Map("whiteSpace" -> "nowrap"))
      ,
      ^.dangerouslySetInnerHTML := new InnerHTML {
        override val __html = props.htmlText
      }
    )()
  }

  def makeHtmlText(text: String): String = text
    .replace("\n", "<br/>")
    .replace("\t", "&nbsp&nbsp&nbsp&nbsp")

  trait InnerHTML extends js.Object {
    val __html: js.UndefOr[String] = js.undefined
  }

  object HTMLVirtualDOMAttributes {

    import VirtualDOMAttributes.Type._

    case class InnerHTMLAttributeSpec(name: String) extends AttributeSpec {
      def :=(innerHtml: InnerHTML): Attribute[InnerHTML] =
        Attribute(name = name, value = innerHtml, AS_IS)
    }
  }

  implicit class HTMLVirtualDOMAttributes(attributes: VirtualDOMAttributes) {

    import HTMLVirtualDOMAttributes._

    lazy val dangerouslySetInnerHTML = InnerHTMLAttributeSpec("dangerouslySetInnerHTML")
  }
}
