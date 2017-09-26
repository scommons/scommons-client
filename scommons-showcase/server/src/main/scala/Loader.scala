
import controllers.{AssetsComponents, ShowcaseController}
import play.api.ApplicationLoader.Context
import play.api.mvc._
import play.api._
import router.Routes

class Loader extends ApplicationLoader {
  def load(context: Context): Application = new ShowcaseComponents(context).application
}

class ShowcaseComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with ControllerComponents
  with AssetsComponents
  with NoHttpFiltersComponents {

  val actionBuilder: ActionBuilder[Request, AnyContent] = defaultActionBuilder
  val parsers: PlayBodyParsers = playBodyParsers

  val controller = new ShowcaseController(this)
  val router = new Routes(httpErrorHandler, assets, controller)
}
