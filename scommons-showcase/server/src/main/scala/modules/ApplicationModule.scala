package modules

import controllers.ShowcaseController
import play.api.mvc.ControllerComponents
import scaldi.Module

class ApplicationModule extends Module {

  bind[ShowcaseController] to new ShowcaseController(
    inject[ControllerComponents]
  )
}
