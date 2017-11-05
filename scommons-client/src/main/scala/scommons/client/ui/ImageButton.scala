package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent

case class ImageButtonProps(data: ImageButtonData,
                            onClick: ImageButtonData => Unit,
                            disabled: Boolean = false,
                            showTextAsTitle: Boolean = false)

object ImageButton {

  type ImageButtonSelf = Self[ImageButtonProps, Unit]

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ImageButtonProps, Unit] { self =>
    val props = self.props.wrapped
    val data = props.data

    val image = if (props.disabled) data.disabledImage.getOrElse("") else data.image
    val primaryClass = if (data.primary) "btn-primary" else ""

    val attributes = (
      if (props.showTextAsTitle) Some(^.title := data.text)
      else None
    ) :: List(
      ^.className := s"btn $primaryClass",
      ^.disabled := props.disabled,
      ^.onClick := onClick(self)
    )

    <.button(attributes)(
      if (props.showTextAsTitle) ImageLabelWrapper(image, None)
      else ImageLabelWrapper(image, Some(data.text))
    )
  }

  private def onClick(self: ImageButtonSelf): MouseSyntheticEvent => Unit = { _ =>
    val props = self.props.wrapped
    props.onClick(props.data)
  }
}
