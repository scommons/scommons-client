package controllers

import play.twirl.api.StringInterpolation
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

class ShowcaseController(protected val controllerComponents: ControllerComponents) extends BaseController {

  val index: Action[AnyContent] = {
    val scriptUrl = bundleUrl("scommons-showcase-client")
    val result =
      Ok(
        html"""<!doctype html>
        <html>
          <head></head>
          <body>
            <div id="root"></div>

            <script src="$scriptUrl"></script>
          </body>
        </html>
      """
      )

    Action(result)
  }

  private def bundleUrl(projectName: String): String = {
    val name = projectName.toLowerCase

    Seq(s"$name-opt-bundle.js", s"$name-fastopt-bundle.js")
      .find(name => getClass.getResource(s"/public/$name") != null)
      .map(controllers.routes.Assets.versioned(_).url)
      .getOrElse(throw new IllegalArgumentException(
        s"Could not find main client script, projectName: $projectName"
      ))
  }
}
