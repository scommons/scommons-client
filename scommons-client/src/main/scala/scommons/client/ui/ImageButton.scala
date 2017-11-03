package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

case class ImageButtonProps(image: String,
                            text: Option[String],
                            disabledImage: Option[String] = None,
                            disabled: Boolean = false,
                            primary: Boolean = false)

object ImageButton {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ImageButtonProps, Unit] { self =>
    val props = self.props.wrapped

    val image = if (props.disabled) props.disabledImage.getOrElse("") else props.image
    val primaryClass = if (props.primary) "btn-primary" else ""

    <.button(
      ^.className := s"btn $primaryClass",
      ^.disabled := props.disabled
    )(
      ImageLabelWrapper(image, props.text)
    )
  }
}
