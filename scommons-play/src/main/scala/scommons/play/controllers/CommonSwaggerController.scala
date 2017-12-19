package scommons.play.controllers

import javax.inject.Inject

import play.api.mvc.{AbstractController, ControllerComponents}

class CommonSwaggerController @Inject() (components: ControllerComponents)
  extends AbstractController(components) {

  def swaggerUi(prefix: String) = Action {
    Redirect(
      url = s"$prefix/assets/lib/swagger-ui/index.html",
      queryString = Map("url" -> Seq(s"$prefix/api-docs"))
    )
  }
}
