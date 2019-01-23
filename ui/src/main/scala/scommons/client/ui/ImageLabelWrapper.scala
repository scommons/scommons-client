package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.statictags.Element

/**
  * Common implementation for image with optional label.
  */
object ImageLabelWrapper {

  private val alignedTextStyle = Map(
    "paddingLeft" -> "3px",
    "verticalAlign" -> "middle"
  )

  private val nonAlignedTextStyle = alignedTextStyle - "verticalAlign"

  def apply(image: String, maybeText: Option[String], alignText: Boolean = true): List[Element] = {
    val imageElement = <.img(^.className := image, ^.src := "")()

    maybeText match {
      case None => List(imageElement)
      case Some(text) => List(
        imageElement,
        <.span(^.style := (if (alignText) alignedTextStyle else nonAlignedTextStyle))(text)
      )
    }
  }
}
