package modules

import controllers.ShowcaseController
import play.api.mvc.ControllerComponents
import scaldi.Module

class ApplicationModule extends Module
  with RepoModule
  with FailingModule {

  private implicit lazy val components = inject[ControllerComponents]

  bind[ShowcaseController] to new ShowcaseController()
}
