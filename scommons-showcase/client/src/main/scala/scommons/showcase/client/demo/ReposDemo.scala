package scommons.showcase.client.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._

object ReposDemo extends RouterProps {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit] { _ =>
    <.div()(
      "Repos",
      <.Route(
        //^.path := s"${self.props.`match`.path}/:id",
        ^.path := "/repos/:id",
        ^.component := Repo()
      )()
    )
  }
}

object Repo extends RouterProps {

  // Params has type of js.Dictionary[String]
  private def id(self: Self[_, _]): String = self.props.`match`.params("id")

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit] { self =>
    <.div(^.id := s"repo-${id(self)}")(s"Repo ${id(self)}")
  }
}
