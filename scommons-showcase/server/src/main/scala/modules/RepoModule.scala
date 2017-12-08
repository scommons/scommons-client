package modules

import akka.actor.ActorSystem
import apis.RepoApiImpl
import controllers.RepoController
import domain.dao.RepoDao
import play.api.mvc.ControllerComponents
import scaldi.Module
import services.RepoService

trait RepoModule extends Module {

  private implicit lazy val components = inject[ControllerComponents]
  private implicit lazy val ec = inject[ActorSystem].dispatcher

  bind[RepoDao] to new RepoDao

  bind[RepoService] to new RepoService(
    inject[RepoDao]
  )

  bind[RepoApiImpl] to new RepoApiImpl(
    inject[RepoService]
  )

  bind[RepoController] to new RepoController(
    inject[RepoApiImpl]
  )
}
