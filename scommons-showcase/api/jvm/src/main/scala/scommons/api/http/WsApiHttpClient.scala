package scommons.api.http

import java.util.concurrent.TimeoutException

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.ByteString
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.api.libs.ws.{BodyWritable, InMemoryBody, StandaloneWSRequest, StandaloneWSResponse}
import scommons.api.http.WsApiHttpClient._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class WsApiHttpClient(baseUrl: String,
                      defaultTimeout: FiniteDuration = 30.seconds)
                     (implicit system: ActorSystem)
  extends ApiHttpClient(baseUrl, defaultTimeout)(system.dispatcher) {

  private implicit val ec: ExecutionContext = system.dispatcher
  private implicit val materializer = ActorMaterializer()

  private[http] val ws = StandaloneAhcWSClient()

  system.registerOnTermination {
    ws.close()
  }

  protected[http] def execute(method: String,
                              targetUrl: String,
                              params: List[(String, String)],
                              jsonBody: Option[String],
                              timeout: FiniteDuration): Future[Option[ApiHttpResponse]] = {

    val req: StandaloneWSRequest = jsonBody match {
      case None => ws.url(targetUrl)
      case Some(body) => ws.url(targetUrl)
        .withBody(body)(writeableOfJsonString)
    }

    execute(req.withMethod(method)
      .withFollowRedirects(true)
      .withQueryStringParameters(params: _*)
      .withRequestTimeout(timeout)
    ).map { resp =>
      Some(ApiHttpResponse(resp.status, resp.body))
    }.recover {
      case _: TimeoutException => None
    }
  }

  private[http] def execute(req: StandaloneWSRequest): Future[StandaloneWSResponse] = {
    req.execute()
  }
}

object WsApiHttpClient {

  private val writeableOfJsonString: BodyWritable[String] = {
    BodyWritable(str => InMemoryBody(ByteString.fromString(str)), "application/json")
  }
}
