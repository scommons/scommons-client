package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent

case class ImageButtonProps(image: String,
                            text: Option[String],
                            onClick: () => Unit,
                            disabledImage: Option[String] = None,
                            disabled: Boolean = false,
                            primary: Boolean = false)

object ImageButton {

  type ImageButtonSelf = Self[ImageButtonProps, Unit]

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ImageButtonProps, Unit] { self =>
    val props = self.props.wrapped

    val image = if (props.disabled) props.disabledImage.getOrElse("") else props.image
    val primaryClass = if (props.primary) "btn-primary" else ""

    <.button(
      ^.className := s"btn $primaryClass",
      ^.disabled := props.disabled,
      ^.onClick := onClick(self)
    )(
      ImageLabelWrapper(image, props.text)
    )
  }

  private def onClick(self: ImageButtonSelf): MouseSyntheticEvent => Unit = { _ =>
    val props = self.props.wrapped
    props.onClick()
  }
}
