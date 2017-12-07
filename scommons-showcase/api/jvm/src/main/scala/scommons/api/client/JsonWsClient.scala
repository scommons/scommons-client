package scommons.api.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.ByteString
import play.api.libs.json._
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.api.libs.ws.{BodyWritable, InMemoryBody, StandaloneWSRequest, StandaloneWSResponse}
import scommons.api.client.JsonWsClient._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

abstract class JsonWsClient(baseUrl: String,
                            defaultTimeout: FiniteDuration = 30.seconds)(implicit system: ActorSystem) {

  private implicit val ec: ExecutionContext = system.dispatcher
  private implicit val materializer = ActorMaterializer()
  private[client] val ws = StandaloneAhcWSClient()

  system.registerOnTermination {
    ws.close()
  }

  def execGet[R](url: String,
                 params: List[(String, String)] = Nil,
                 timeout: FiniteDuration = defaultTimeout
                )(implicit jsonReads: Reads[R]): Future[R] = {

    exec[String, R]("GET", url, params, None, timeout)
  }

  def execPost[D, R](url: String,
                     data: D,
                     params: List[(String, String)] = Nil,
                     timeout: FiniteDuration = defaultTimeout
                    )(implicit jsonWrites: Writes[D], jsonReads: Reads[R]): Future[R] = {

    exec("POST", url, params, Some(data), timeout)
  }

  def execPut[D, R](url: String,
                    data: D,
                    params: List[(String, String)] = Nil,
                    timeout: FiniteDuration = defaultTimeout
                   )(implicit jsonWrites: Writes[D], jsonReads: Reads[R]): Future[R] = {

    exec("PUT", url, params, Some(data), timeout)
  }

  def execDelete[D, R](url: String,
                       data: Option[D] = None,
                       params: List[(String, String)] = Nil,
                       timeout: FiniteDuration = defaultTimeout
                      )(implicit jsonWrites: Writes[D], jsonReads: Reads[R]): Future[R] = {

    exec("DELETE", url, params, data, timeout)
  }

  private def exec[T, R](method: String,
                         url: String,
                         params: List[(String, String)],
                         data: Option[T],
                         timeout: FiniteDuration
                        )(implicit jsonWrites: Writes[T], jsonReads: Reads[R]): Future[R] = {

    val targetUrl = getTargetUrl(baseUrl, url)

    execute(
      method,
      targetUrl,
      params,
      data.map { d =>
        Json.stringify(Json.toJson(d))
      },
      timeout
    ).map(parseResponse(targetUrl, _))
  }

  private def execute(method: String,
                      targetUrl: String,
                      params: List[(String, String)],
                      jsonBody: Option[String],
                      timeout: FiniteDuration
                     ): Future[StandaloneWSResponse] = {

    val req: StandaloneWSRequest = jsonBody match {
      case None => ws.url(targetUrl)
      case Some(body) => ws.url(targetUrl)
        .withBody(body)(writeableOfJsonString)
    }

    execute(req.withMethod(method)
      .withFollowRedirects(true)
      .withQueryStringParameters(params: _*)
      .withRequestTimeout(timeout))
  }

  private[client] def execute(req: StandaloneWSRequest): Future[StandaloneWSResponse] = {
    req.execute()
  }

  private[client] def parseResponse[R](url: String, response: StandaloneWSResponse)
                                      (implicit jsonReads: Reads[R]): R = response match {

    case res if res.status <= 299 =>
      val body = res.body
      Json.parse(body).validate[R] match {
        case JsSuccess(data, _) => data
        case JsError(error) =>
          val err =
            s"""Error parsing http response:
               |url: $url
               |status: ${res.status}
               |error: $error
               |body: $body""".stripMargin
          throw new Exception(err)
      }

    case other =>
      val body = other.body
      val maybeData =
        if (body.trim.startsWith("{")) {
          Json.parse(body).validate[R] match {
            case JsSuccess(data, _) => Some(data)
            case _ => None
          }
        }
        else None

      maybeData match {
        case Some(data) => data
        case None =>
          throw new Exception(
            s"""Received error response:
               |url: $url
               |status: ${other.status}
               |body: $body""".stripMargin)
      }
  }
}

object JsonWsClient {

  private val writeableOfJsonString: BodyWritable[String] = {
    BodyWritable(str => InMemoryBody(ByteString.fromString(str)), "application/json")
  }

  def queryParams(params: (String, Option[_])*): List[(String, String)] = params.collect {
    case (p, Some(v)) => (p, v.toString)
  }.toList

  private[client] def getTargetUrl(baseUrl: String, url: String): String = {
    val normalizedUrl =
      if (url.startsWith("/"))
        url.substring(1)
      else
        url

    if (baseUrl.endsWith("/"))
      s"$baseUrl$normalizedUrl"
    else
      s"$baseUrl/$normalizedUrl"
  }
}
