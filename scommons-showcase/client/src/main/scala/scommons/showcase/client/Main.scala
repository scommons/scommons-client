package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.scalajs.dom
import scommons.client.app._

import scala.scalajs.js.JSApp

object Main extends JSApp {

  def main(): Unit = {
    val mountNode = dom.document.getElementById("root")

    dom.document.title = "scommons-showcase"

    ReactDOM.render(<(AppMainPanel.reactClass)(^.wrapped := AppMainPanelProps(
      name = "scommons-showcase",
      user = "me",
      copyright = "© scommons-showcase",
      version = "(version: 0.1.0-SNAPSHOT)"
    ))(
      <(AppBrowsePanel.reactClass)()()
    ), mountNode)
  }
}
