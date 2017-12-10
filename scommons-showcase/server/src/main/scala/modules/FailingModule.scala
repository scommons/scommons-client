package modules

import akka.actor.ActorSystem
import apis.FailingApiImpl
import controllers.FailingController
import play.api.mvc.ControllerComponents
import scaldi.Module

trait FailingModule extends Module {

  private implicit lazy val components = inject[ControllerComponents]
  private implicit lazy val ec = inject[ActorSystem].dispatcher

  bind[FailingApiImpl] to new FailingApiImpl()

  bind[FailingController] to new FailingController(
    inject[FailingApiImpl]
  )
}
