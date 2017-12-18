package scommons.play.controllers

import java.io.{PrintWriter, StringWriter}

import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json._
import play.api.mvc._
import scommons.api._
import scommons.play.apis.ex._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

abstract class BaseApiController(components: ControllerComponents) extends AbstractController(components) {

  protected val logger: Logger = LoggerFactory.getLogger(getClass)

  def apiNoBodyAction[R <: ApiResponse](block: => Future[R])
                                       (implicit
                                        writes: Writes[R],
                                        ec: ExecutionContext
                                       ): Action[AnyContent] = {

    anyAction(actionAsync[AnyContent](_, { _ =>
      block.map { resp =>
        Ok(Json.toJson(resp))
      }
    }))
  }

  def apiAction[T, R <: ApiResponse](block: T => Future[R])
                                    (implicit
                                     reads: Reads[T],
                                     writes: Writes[R],
                                     ec: ExecutionContext
                                    ): Action[JsValue] = {

    jsonAction(actionAsync[JsValue](_, { request =>
      parseJsonRequest[T](request, { data =>
        block(data).map { resp =>
          Ok(Json.toJson(resp))
        }
      })
    }))
  }

  private[controllers] def anyAction(block: Request[AnyContent] => Future[Result]): Action[AnyContent] = {
    Action.async(block)
  }

  private[controllers] def jsonAction(block: Request[JsValue] => Future[Result]): Action[JsValue] = {
    Action.async(parse.json)(block)
  }

  def parseJsonRequest[T](request: Request[JsValue],
                          block: T => Future[Result]
                         )(implicit reads: Reads[T]): Future[Result] = {

    request.body.validate[T].fold(
      errors =>
        Future.successful(BadRequest(Json.toJson(StatusResponse(ApiStatus(400,
          "Failed to parse request body", errors.mkString("\n")))))),
      data =>
        block(data)
    )
  }

  def actionAsync[T](request: Request[T],
                     block: Request[T] => Future[Result])
                    (implicit ec: ExecutionContext): Future[Result] = {

    val futureResult = try {
      block(request)
    }
    catch {
      case e: IllegalArgumentException => Future.successful(BadRequest(Json.toJson(StatusResponse(ApiStatus(400,
        e.getMessage)))))
      case NonFatal(e) => Future.successful(handleError("Failed to handle request", request, e))
    }

    futureResult.recover {
      case NonFatal(e) => handleError("Error while processing request", request, e)
    }
  }

  def handleError(error: String, request: Request[_], t: Throwable): Result = t match {
    case e: NotFoundStatusException => NotFound(Json.toJson(StatusResponse(e.status)))
    case e: ConflictStatusException => Conflict(Json.toJson(StatusResponse(e.status)))
    case e: BadRequestStatusException => BadRequest(Json.toJson(StatusResponse(e.status)))
    case e: InternalServerErrorStatusException => InternalServerError(Json.toJson(StatusResponse(e.status)))
    case e =>
      logger.error(s"$error: $request", e)

      val stackTrace = new StringWriter
      e.printStackTrace(new PrintWriter(stackTrace, true))

      InternalServerError(Json.toJson(StatusResponse(ApiStatus(500, error,
        s"Request: $request\n\nError: $stackTrace"))))
  }
}
