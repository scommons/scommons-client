package scommons.client.showcase.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.Buttons
import scommons.client.ui.tab._

object TabPanelDemo {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit] { _ =>
    val nestedItems = List(
      TabItemData("Nested Tab 1", image = Some(Buttons.ADD.image), render = Some { _ =>
        <.div()("Content for nested tab 1")
      }),
      TabItemData("Nested Tab 2", image = Some(Buttons.REMOVE.image), render = Some { _ =>
        <.div()("Content for nested tab 2")
      })
    )

    val nestedTabs = React.createClass[Unit, Unit] { _ =>
      <(TabPanel())(^.wrapped := TabPanelProps(nestedItems))()
    }

    val items = List(
      TabItemData("First Tab", render = Some { _ =>
        <.div()("Content for first tab")
      }),
      TabItemData("Second Tab", image = Some(Buttons.FIND.image), component = Some(nestedTabs)),
      TabItemData("Third Tab", render = Some { _ =>
        <.div()("Content for third tab")
      })
    )

    <.div()(
      <.h2()("TabPanel"),
      <.p()("Demonstrates tabs functionality."),
      <.p()(
        <(TabPanel())(^.wrapped := TabPanelProps(items))()
      ),
      <.h3()("Tabs on the left"),
      <.p()(
        <(TabPanel())(^.wrapped := TabPanelProps(items, selectedIndex = 1, direction = TabDirection.Left))()
      ),
      <.h3()("Tabs at the bottom"),
      <.p()(
        <(TabPanel())(^.wrapped := TabPanelProps(items, selectedIndex = 2, direction = TabDirection.Bottom))()
      )
    )
  }
}
