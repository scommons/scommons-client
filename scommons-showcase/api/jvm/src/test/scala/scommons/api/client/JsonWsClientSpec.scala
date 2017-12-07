package scommons.api.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.ByteString
import org.mockito.ArgumentCaptor
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers}
import play.api.libs.json._
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.api.libs.ws.{EmptyBody, InMemoryBody, StandaloneWSRequest, StandaloneWSResponse}
import scommons.api.client.JsonWsClient._
import scommons.api.client.JsonWsClientSpec._

import scala.concurrent.Future
import scala.concurrent.duration.{FiniteDuration, _}

class JsonWsClientSpec extends FlatSpec
  with Matchers
  with BeforeAndAfterAll
  with BeforeAndAfterEach
  with MockitoSugar
  with ScalaFutures {

  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(100, Millis))

  private implicit val system = ActorSystem(getClass.getSimpleName)
  private implicit val materializer = ActorMaterializer()

  private val baseUrl = "http://test.api.client"
  private val response = mock[StandaloneWSResponse]

  private class TestWsClient extends JsonWsClient(baseUrl) {

    override private[client] val ws = spy(StandaloneAhcWSClient())

    override private[client] def execute(req: StandaloneWSRequest): Future[StandaloneWSResponse] = {
      Future.successful(response)
    }
  }

  private val client = spy(new TestWsClient())

  private val params = List("p1" -> "1", "p2" -> "2")
  private val timeout = 5.seconds

  override protected def beforeEach(): Unit = {
    reset(client, response)
  }

  override protected def afterAll(): Unit = {
    reset(client.ws)

    system.terminate().futureValue

    verify(client.ws).close()
  }

  it should "execute GET request" in {
    //given
    val url = s"/api/get/url"
    val expectedResult = List(TestRespData(1, "test"))
    when(response.status).thenReturn(200)
    when(response.body).thenReturn(Json.stringify(Json.toJson(expectedResult)))

    //when
    val result = client.execGet[List[TestRespData]](url, params, timeout).futureValue

    //then
    result shouldBe expectedResult

    assertRequest[String]("GET", url, params, None, timeout)
  }

  it should "execute POST request" in {
    //given
    val url = s"/api/post/url"
    val data = TestReqData(1)
    val expectedResult = List(TestRespData(2, "test"))
    when(response.status).thenReturn(200)
    when(response.body).thenReturn(Json.stringify(Json.toJson(expectedResult)))

    //when
    val result = client.execPost[TestReqData, List[TestRespData]](url, data, params, timeout).futureValue

    //then
    result shouldBe expectedResult

    assertRequest("POST", url, params, Some(data), timeout)
  }

  it should "execute PUT request" in {
    //given
    val url = s"/api/put/url"
    val data = TestReqData(1)
    val expectedResult = List(TestRespData(2, "test"))
    when(response.status).thenReturn(200)
    when(response.body).thenReturn(Json.stringify(Json.toJson(expectedResult)))

    //when
    val result = client.execPut[TestReqData, List[TestRespData]](url, data, params, timeout).futureValue

    //then
    result shouldBe expectedResult

    assertRequest("PUT", url, params, Some(data), timeout)
  }

  it should "execute DELETE request" in {
    //given
    val url = s"/api/delete/url"
    val data = TestReqData(1)
    val expectedResult = List(TestRespData(2, "test"))
    when(response.status).thenReturn(200)
    when(response.body).thenReturn(Json.stringify(Json.toJson(expectedResult)))

    //when
    val result = client.execDelete[TestReqData, List[TestRespData]](url, Some(data), params, timeout).futureValue

    //then
    result shouldBe expectedResult

    assertRequest("DELETE", url, params, Some(data), timeout)
  }

  it should "convert list of optional params when queryParams" in {
    //when & then
    queryParams("test" -> None) shouldBe Nil
    queryParams("test" -> None, "test2" -> Some(2)) shouldBe List("test2" -> "2")
  }

  it should "return normalized target url when getTargetUrl" in {
    //when & then
    getTargetUrl("http://test.com", "some/url") shouldBe "http://test.com/some/url"
    getTargetUrl("http://test.com/", "some/url") shouldBe "http://test.com/some/url"
    getTargetUrl("http://test.com", "/some/url") shouldBe "http://test.com/some/url"
    getTargetUrl("http://test.com/", "/some/url") shouldBe "http://test.com/some/url"
  }

  it should "fail if invalid json when parseResponse" in {
    //given
    val url = s"/some/url"
    val statusCode = 200
    val data = """{"id": 1, "missing": "name"}"""
    when(response.status).thenReturn(statusCode)
    when(response.body).thenReturn(data)

    //when
    val ex = the[Exception] thrownBy {
      client.parseResponse[TestRespData](url, response)
    }

    //then
    val message = ex.getMessage
    message should include(s"url: $url")
    message should include(s"status: $statusCode")
    message should include("error.path.missing")
    message should include(s"body: $data")
  }

  it should "fail if error status when parseResponse" in {
    //given
    val url = s"/some/url"
    val statusCode = 300
    val data = "testData"
    when(response.status).thenReturn(statusCode)
    when(response.body).thenReturn(data)

    //when
    val ex = the[Exception] thrownBy {
      client.parseResponse[List[TestRespData]](url, response)
    }

    //then
    val message = ex.getMessage
    message should include(s"url: $url")
    message should include(s"status: $statusCode")
    message should include(s"body: $data")
  }

  it should "parse json for HTTP success response when parseResponse" in {
    //given
    val respData = List(TestRespData(1, "test"))
    when(response.status).thenReturn(200)
    when(response.body).thenReturn(Json.stringify(Json.toJson(respData)))

    //when
    val result = client.parseResponse[List[TestRespData]](s"/api/url", response)

    //then
    result shouldBe respData
  }

  it should "parse json for HTTP failure response when parseResponse" in {
    //given
    val respData = TestRespData(1, "test")
    when(response.status).thenReturn(500)
    when(response.body).thenReturn(Json.stringify(Json.toJson(respData)))

    //when
    val result = client.parseResponse[TestRespData](s"/api/url", response)

    //then
    result shouldBe respData
  }

  private def assertRequest[T](method: String,
                               url: String,
                               params: List[(String, String)],
                               data: Option[T],
                               timeout: FiniteDuration)
                              (implicit jsonWrites: Writes[T]): Unit = {

    val reqCaptor = ArgumentCaptor.forClass(classOf[StandaloneWSRequest])
    verify(client).execute(reqCaptor.capture())

    val req = reqCaptor.getValue
    req.method shouldBe method
    req.url shouldBe s"$baseUrl$url"
    req.followRedirects shouldBe Some(true)
    req.requestTimeout shouldBe Some(timeout.toMillis)
    req.queryString shouldBe params.foldLeft(Map.empty[String, Seq[String]]) {
      case (m, (k, v)) => m + (k -> (v +: m.getOrElse(k, Nil)))
    }

    req.contentType shouldBe data.map(_ =>"application/json")
    req.body shouldBe data.map { d =>
      val jsonBody = Json.stringify(Json.toJson(d))
      InMemoryBody(ByteString.fromString(jsonBody))
    }.getOrElse(EmptyBody)
  }
}

object JsonWsClientSpec {

  case class TestReqData(id: Int)

  object TestReqData {
    implicit val jsonFormat: Format[TestReqData] = Json.format[TestReqData]
  }

  case class TestRespData(id: Int, name: String)

  object TestRespData {
    implicit val jsonFormat: Format[TestRespData] = Json.format[TestRespData]
  }
}
