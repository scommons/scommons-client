package scommons.client.showcase.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.ButtonImagesCss
import scommons.client.ui.list._

object ListDemo {
  
  def apply(): ReactClass = reactClass
  private lazy val reactClass = createComp
  
  private def createComp = React.createClass[Unit, Unit] { _ =>
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
