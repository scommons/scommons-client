package scommons.api.client

import org.scalajs.dom
import play.api.libs.json._
import scommons.api.client.JsonJsClient._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}
import scala.scalajs.js

abstract class JsonJsClient(baseUrl: String,
                            defaultTimeout: FiniteDuration = 30.seconds) {

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

  private[client] def execute(method: String,
                              targetUrl: String,
                              params: List[(String, String)],
                              jsonBody: Option[String],
                              timeout: FiniteDuration
                             ): Future[dom.XMLHttpRequest] = {

    val (data: String, headers) = jsonBody match {
      case None =>
        (null, Map.empty[String, String])
      case Some(body) =>
        (body, Map("Content-Type" -> "application/json"))
    }

    val req = new dom.XMLHttpRequest()
    val promise = Promise[dom.XMLHttpRequest]()

    req.onreadystatechange = { (_: dom.Event) =>
      if (req.readyState == 4) {
        promise.success(req)
      }
    }

    def enc(p: String) = js.URIUtils.encodeURIComponent(p)

    def uri(url: String): String = {
      val queryString = params.foldLeft(Map.empty[String, Seq[String]]) {
        case (m, (k, v)) => m + (k -> (v +: m.getOrElse(k, Nil)))
      }

      if (queryString.isEmpty) url
      else {
        val qs = (for {
          (n, vs) <- queryString
          v <- vs
        } yield s"${enc(n)}=${enc(v)}").mkString("&")
        s"$url?$qs"
      }
    }

    req.open(method, uri(targetUrl))
    req.timeout = timeout.toMillis.toInt

    headers.foreach(x => req.setRequestHeader(x._1, x._2))

    if (data == null) req.send()
    else req.send(data)

    promise.future
  }

  private[client] def parseResponse[R](url: String, response: dom.XMLHttpRequest)
                                      (implicit jsonReads: Reads[R]): R = response match {

    case res if res.status == 0 =>
      throw new Exception(
        s"""Request timed out, unable to get timely response for:
           |url: $url""".stripMargin)

    case res if res.status <= 299 =>
      val body = res.responseText
      Json.parse(body).validate[R] match {
        case JsSuccess(data, _) => data
        case JsError(error) =>
          throw new Exception(
            s"""Error parsing http response:
               |url: $url
               |status: ${res.status}
               |error: $error
               |body: $body""".stripMargin)
      }

    case other =>
      val body = other.responseText
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

object JsonJsClient {

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
