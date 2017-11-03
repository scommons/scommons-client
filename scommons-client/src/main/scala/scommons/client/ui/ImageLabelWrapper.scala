package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

/**
  * Common implementation for image with label.
  */
object ImageLabelWrapper {

  def apply(image: String, maybeText: Option[String]): List[ReactElement] = {
    val imageElement = <.img(^.className := image, ^.src := "")()

    maybeText match {
      case None => List(imageElement)
      case Some(text) =>
        val textStyle = Map(
          "paddingLeft" -> "3px",
          "verticalAlign" -> "middle"
        )

        List(
          imageElement,
          <.span(^.style := textStyle)(text)
        )
    }
  }
}
