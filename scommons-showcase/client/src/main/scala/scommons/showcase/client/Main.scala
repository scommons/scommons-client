package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom
import scommons.client.app._
import scommons.client.browsetree.{BrowseTreeItemData, BrowseTreeNodeData}

import scala.scalajs.js.JSApp

object Main extends JSApp {

  def main(): Unit = {
    val mountNode = dom.document.getElementById("root")

    dom.document.title = "scommons-showcase"

    val reposItem = BrowseTreeItemData("Repos")
    val aboutNode = BrowseTreeNodeData("About", List(reposItem))

    ReactDOM.render(<.HashRouter()(
      <(AppMainPanel())(^.wrapped := AppMainPanelProps(
        name = "scommons-showcase",
        user = "me",
        copyright = "© scommons-showcase",
        version = "(version: 0.1.0-SNAPSHOT)"
      ))(
        <(AppBrowsePanel())(^.wrapped := AppBrowsePanelProps(
          List(aboutNode),
          Map(
            aboutNode.key -> BrowsePanel("/about", About(_: Props[_])),
            reposItem.key -> BrowsePanel("/repos", Repos(_: Props[_]))
          )
        ))()
      )
    ), mountNode)
  }
}

object About extends RouterProps {

  def apply(props: Props[_]): ReactElement = {
    println(s"about: location.pathname: ${props.location.pathname}")
    println(s"about: match.path: ${props.`match`.path}")

    <.div()("About")
  }
}

object Repos extends RouterProps {

  def apply(props: Props[_]): ReactElement = {
    println(s"repos: location.pathname: ${props.location.pathname}")
    println(s"repos: match.path: ${props.`match`.path}")

    <.div()(
      "Repos",
      <.Route(
        ^.path := s"${props.`match`.path}/:id",
        ^.component := Repo()
      )()
    )
  }
}

object Repo extends RouterProps {

  // Params has type of js.Dictionary[String]
  private def id(self: Self[_, _]): String = self.props.`match`.params("id")

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    (self) => <.div(^.id := s"repo-${id(self)}")(s"Repo ${id(self)}")
  )
}
