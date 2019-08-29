package scommons.client.showcase.demo

import scommons.client.ui.ButtonImagesCss
import scommons.client.ui.list._
import scommons.react._

object ListDemo extends FunctionComponent[Unit] {

  protected def render(props: Props): ReactElement = {
    <.div()(
      <.h2()("ListBox"),
      <.p()("Demonstrates list box functionality."),
      <.p()(
        <(ListBox())(^.wrapped := ListBoxProps(
          items = List(
            ListBoxData("chocolate", "Chocolate", Some(ButtonImagesCss.acceptDisabled)),
            ListBoxData("strawberry", "Strawberry"),
            ListBoxData("vanilla", "Vanilla", Some(ButtonImagesCss.accept))
          )
        ))()
      ),
      
      <.hr.empty,
      <.h2()("PickList"),
      <.p()("PickList is two-lists component that allows visually add or remove items."),
      <.p()(
        <(PickList())(^.wrapped := PickListProps(
          items = List(
            ListBoxData("chocolate", "Chocolate", Some(ButtonImagesCss.acceptDisabled)),
            ListBoxData("strawberry", "Strawberry"),
            ListBoxData("vanilla", "Vanilla", Some(ButtonImagesCss.accept))
          ),
          selectedIds = Set("vanilla"),
          preSelectedIds = Set("chocolate")
        ))()
      )
    )
  }
}
