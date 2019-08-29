package scommons.client.showcase.demo

import scommons.client.ui.ButtonImagesCss._
import scommons.client.ui._
import scommons.client.util.ActionsData
import scommons.react._

object ButtonsDemo extends FunctionComponent[Unit] {

  protected def render(props: Props): ReactElement = {
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
          ActionsData.empty.copy(enabledCommands = Set(Buttons.ADD.command, "primary"))
        ))()
      ),
      <.h3()("Group"),
      <.p()(
        <(ButtonsPanel())(^.wrapped := ButtonsPanelProps(
          imageButtons,
          ActionsData.empty.copy(enabledCommands = Set(Buttons.ADD.command, "primary")),
          group = true
        ))()
      )
    )
  }
}
