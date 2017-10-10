package scommons.client.browsetree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.browsetree.BrowseTreeCss._

object BrowseTree {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit] { self =>
    <.div(^.className := browseTree)(
      self.props.children
    )
  }
}
