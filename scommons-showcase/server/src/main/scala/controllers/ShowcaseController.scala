package controllers

import play.twirl.api.StringInterpolation
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

class ShowcaseController(protected val controllerComponents: ControllerComponents) extends BaseController {

  val index: Action[AnyContent] = {
    val scriptUrl = bundleUrl("scommons-showcase-client")
    val scriptUrlPrefix = scriptUrl.take(scriptUrl.lastIndexOf(".js"))

    val result =
      Ok(
        html"""<!doctype html>
          <html>
            <head>
              <meta charset="UTF-8" />
              <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
              <!--link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"-->
              <!--link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-responsive.min.css"-->
              <link rel="stylesheet" href="/scommons-showcase/assets/lib/scommons-client/css/bootstrap.min.css" />
              <link rel="stylesheet" href="/scommons-showcase/assets/lib/scommons-client/css/bootstrap-responsive.min.css" />
              <!--link rel="stylesheet" href="/build/styles.css" /-->
              <link rel="stylesheet" href="/scommons-showcase/assets/lib/scommons-client/css/custom.css" />
            </head>
            <body>
              <div id="root">Loading, please, wait...</div>

              <!--script src="http://code.jquery.com/jquery-2.1.3.min.js"></script-->
              <!--script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.js"></script-->
              <script src="/scommons-showcase/assets/lib/scommons-client/js/jquery-1.9.1.min.js"></script>
              <script src="/scommons-showcase/assets/lib/scommons-client/js/bootstrap.min.js"></script>

              <script src="$scriptUrlPrefix-library.js"></script>
              <script src="$scriptUrlPrefix-loader.js"></script>
              <script src="$scriptUrl"></script>
            </body>
          </html>
          """
      )

    Action(result)
  }

  private def bundleUrl(projectName: String): String = {
    val name = projectName.toLowerCase

    Seq(s"$name-opt.js", s"$name-fastopt.js")
      .find(name => getClass.getResource(s"/public/$name") != null)
      .map(controllers.routes.Assets.versioned(_).url)
      .getOrElse(throw new IllegalArgumentException(
        s"Could not find main client script, projectName: $projectName"
      ))
  }
}
