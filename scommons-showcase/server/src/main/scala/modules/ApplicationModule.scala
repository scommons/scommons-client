package modules

import akka.actor.ActorSystem
import controllers.{ShowcaseController, ShowcaseRepoController}
import play.api.mvc.ControllerComponents
import scaldi.Module

import scala.concurrent.ExecutionContext

class ApplicationModule extends Module {

  private implicit lazy val components: ControllerComponents = inject[ControllerComponents]
  private implicit lazy val ec: ExecutionContext = inject[ActorSystem].dispatcher

  bind[ShowcaseController] to new ShowcaseController()

  bind[ShowcaseRepoController] to new ShowcaseRepoController(null)
}
