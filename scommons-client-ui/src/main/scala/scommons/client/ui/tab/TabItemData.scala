package scommons.client.ui.tab

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

case class TabItemData(title: String,
                       image: Option[String] = None,
                       component: Option[ReactClass] = None,
                       render: Option[Props[_] => ReactElement] = None)
