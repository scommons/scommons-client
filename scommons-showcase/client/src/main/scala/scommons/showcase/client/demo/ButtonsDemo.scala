package scommons.showcase.client.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.ButtonImagesCss._
import scommons.client.ui._
import scommons.client.util.ActionsData

object ButtonsDemo {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit] { _ =>
    val imageButtons = List(
      Buttons.ADD,
      Buttons.REMOVE,
      ImageButtonData("primary", accept, acceptDisabled, "Primary", primary = true)
    )

    <.div()(
      <.h2()("Image Buttons"),
      <.hr()(),
      <.p()(
        imageButtons.map { data =>
          <(ImageButton())(^.wrapped := ImageButtonProps(data, () => (), data.command == Buttons.REMOVE.command))()
        }
      ),
      <.h2()("Buttons Panels"),
      <.hr()(),
      <.h3()("Toolbar"),
      <.p()(
        <(ButtonsPanel())(^.wrapped := ButtonsPanelProps(
          imageButtons,
          ActionsData(Set(Buttons.ADD.command, "primary"), _ => ()),
          group = false
        ))()
      ),
      <.h3()("Group"),
      <.p()(
        <(ButtonsPanel())(^.wrapped := ButtonsPanelProps(
          imageButtons,
          ActionsData(Set(Buttons.ADD.command, "primary"), _ => ()),
          group = true
        ))()
      )
    )
  }
}
