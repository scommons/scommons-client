package modules

import controllers.ShowcaseController
import play.api.mvc.ControllerComponents

class ApplicationModule extends RepoModule {

  private implicit lazy val components = inject[ControllerComponents]

  bind[ShowcaseController] to new ShowcaseController()
}
