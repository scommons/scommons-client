package controllers

import play.twirl.api.StringInterpolation
import play.api.mvc._

class ShowcaseController()(implicit components: ControllerComponents)
  extends AbstractController(components) {

  lazy val index: Action[AnyContent] = {
    val projectName = "scommons-showcase-client"
    val mainScript = scriptUrl(projectName, ".js")
    val loaderScript = scriptUrl(projectName, "-loader.js")
    val libraryScript = scriptUrl(projectName, "-library.js")
    val assetsUrl = "/scommons-showcase/assets"

    val result =
      Ok(
        html"""<!doctype html>
          <html>
            <head>
              <meta charset="UTF-8" />
              <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
              <!--link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"-->
              <!--link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-responsive.min.css"-->
              <link rel="stylesheet" href="$assetsUrl/lib/scommons-client/css/bootstrap.min.css" />
              <link rel="stylesheet" href="$assetsUrl/lib/scommons-client/css/bootstrap-responsive.min.css" />
              <link rel="stylesheet" href="$assetsUrl/lib/scommons-client/css/custom.css" />
              <link rel="stylesheet" href="$assetsUrl/styles/styles.css" />
            </head>
            <body>
              <div id="root">Loading, please, wait...</div>

              <!--script src="http://code.jquery.com/jquery-2.1.3.min.js"></script-->
              <!--script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.js"></script-->
              <script src="$assetsUrl/lib/scommons-client/js/jquery-1.9.1.min.js"></script>
              <script src="$assetsUrl/lib/scommons-client/js/bootstrap.min.js"></script>

              <script src="$libraryScript"></script>
              <script src="$loaderScript"></script>
              <script src="$mainScript"></script>
            </body>
          </html>
          """
      )

    Action(result)
  }

  private def scriptUrl(projectName: String, suffix: String): String = {
    val name = projectName.toLowerCase

    Seq(s"$name-opt$suffix", s"$name-fastopt$suffix")
      .find(name => getClass.getResource(s"/public/$name") != null)
      .map(controllers.routes.Assets.versioned(_).url)
      .getOrElse(throw new IllegalArgumentException(
        s"Could not find script, projectName: $projectName, suffix: $suffix"
      ))
  }
}
