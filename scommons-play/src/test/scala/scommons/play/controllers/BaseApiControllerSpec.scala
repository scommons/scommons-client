package scommons.play.controllers

import java.io.{PrintWriter, StringWriter}

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import org.mockito.Matchers.{any, eq => mEq}
import org.mockito.Mockito._
import org.mockito.invocation.InvocationOnMock
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.time.{Millis, Seconds, Span}
import org.slf4j.Logger
import play.api.libs.json.Json.toJson
import play.api.libs.json.{Format, JsValue, Json}
import play.api.mvc._
import scommons.api.{ApiStatus, DataResponse, StatusResponse}
import scommons.play.apis.ex._
import scommons.play.controllers.BaseApiControllerSpec.{TestData, TestResp}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BaseApiControllerSpec extends FlatSpec
  with Matchers
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with MockitoSugar
  with ScalaFutures {

  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(100, Millis))

  private implicit val system = ActorSystem(getClass.getSimpleName)
  private implicit val mat = ActorMaterializer(ActorMaterializerSettings(system))

  private val mockLogger = mock[Logger]

  private class TestApiController extends BaseApiController(null) {
    override protected val logger: Logger = mockLogger
  }
  private val controller = spy(new TestApiController)

  override protected def beforeEach(): Unit = {
    reset(controller, mockLogger)
  }

  override protected def afterEach(): Unit = {
    verifyNoMoreInteractions(mockLogger)
  }

  override protected def afterAll(): Unit = {
    system.terminate().futureValue
  }

  private def anyFunc1[T, R] = any.asInstanceOf[T => R]

  it should "process request when apiNoBodyAction" in {
    //given
    val action = mock[Action[AnyContent]]
    val request = mock[Request[AnyContent]]
    var futureResult: Future[Result] = null
    doAnswer((invocation: InvocationOnMock) => {
      val block = invocation.getArguments.head.asInstanceOf[Request[AnyContent] => Future[Result]]
      futureResult = block(request.asInstanceOf[Request[AnyContent]])
      action.asInstanceOf[Action[AnyContent]]
    }).when(controller).anyAction(anyFunc1[Request[AnyContent], Future[Result]])

    //when
    controller.apiNoBodyAction[StatusResponse] {
      Future.successful(StatusResponse.Ok)
    } shouldBe action

    //then
    assertResult(futureResult.futureValue, 200, toJson(StatusResponse.Ok))

    verify(controller).actionAsync(mEq(request), anyFunc1[Request[AnyContent], Future[Result]])(mEq(global))
  }

  it should "process request when apiAction" in {
    //given
    val action = mock[Action[JsValue]]
    val request = mock[Request[JsValue]]
    var futureResult: Future[Result] = null
    doAnswer((invocation: InvocationOnMock) => {
      val block = invocation.getArguments.head.asInstanceOf[Request[JsValue] => Future[Result]]
      futureResult = block(request.asInstanceOf[Request[JsValue]])
      action.asInstanceOf[Action[JsValue]]
    }).when(controller).jsonAction(anyFunc1[Request[JsValue], Future[Result]])

    val data = TestData(1, "test name")
    when(request.body).thenReturn(toJson(data))

    //when
    controller.apiAction[TestData, TestResp] { data =>
      Future.successful(TestResp(data))
    } shouldBe action

    //then
    assertResult(futureResult.futureValue, 200, toJson(TestResp(data)))

    verify(controller).actionAsync(mEq(request), anyFunc1[Request[JsValue], Future[Result]])(mEq(global))
    verify(controller).parseJsonRequest(mEq(request), anyFunc1[TestData, Future[Result]])(mEq(TestData.jsonFormat))
  }

  "parseJsonRequest" should "parse json request" in {
    //given
    val request = mock[Request[JsValue]]
    val data = TestData(1, "test name")
    when(request.body).thenReturn(toJson(data))
    val result = mock[Future[Result]]
    val block = mock[TestData => Future[Result]]
    when(block.apply(any[TestData])).thenReturn(result)

    //when & then
    controller.parseJsonRequest(request, block) shouldBe result

    verify(block).apply(data)
  }

  it should "fail to parse invalid json request" in {
    //given
    val request = mock[Request[JsValue]]
    when(request.body).thenReturn(toJson(StatusResponse.Ok))
    val block = mock[TestData => Future[Result]]

    //when
    val result = controller.parseJsonRequest(request, block).futureValue

    //then
    assertResult(result, 400, toJson(StatusResponse(ApiStatus(400,
      "Failed to parse request body",
        """(/id,List(JsonValidationError(List(error.path.missing),WrappedArray())))
          |(/name,List(JsonValidationError(List(error.path.missing),WrappedArray())))""".stripMargin))))

    verify(block, times(0)).apply(any[TestData])
  }

  "actionAsync" should "fail with 400 status if IllegalArgumentException during request handling" in {
    //given
    val request = mock[Request[AnyContent]]

    //when
    val result = controller.actionAsync(request, { req: Request[AnyContent] =>
      req shouldBe request

      throw new IllegalArgumentException("test exception")
    }).futureValue

    //then
    assertResult(result, 400, toJson(StatusResponse(ApiStatus(400, "test exception"))))

    verifyZeroInteractions(mockLogger)
  }

  it should "fail with 500 status if IllegalArgumentException during request processing" in {
    //given
    val request = mock[Request[AnyContent]]
    val e = new IllegalArgumentException("test exception")

    //when
    val result = controller.actionAsync(request, { req: Request[AnyContent] =>
      req shouldBe request

      Future.failed(e)
    }).futureValue

    //then
    assertResult(result, 500, toJson(StatusResponse(ApiStatus(500, "Error while processing request",
      s"Request: $request\n\nError: ${stackTrace(e)}"))))

    verify(mockLogger).error(s"Error while processing request: $request", e)
  }

  it should "fail with 500 status if Exception during request handling" in {
    //given
    val request = mock[Request[AnyContent]]
    val e = new Exception("test exception")

    //when
    val result = controller.actionAsync(request, { req: Request[AnyContent] =>
      req shouldBe request

      throw e
    }).futureValue

    //then
    assertResult(result, 500, toJson(StatusResponse(ApiStatus(500, "Failed to handle request",
      s"Request: $request\n\nError: ${stackTrace(e)}"))))

    verify(mockLogger).error(s"Failed to handle request: $request", e)
  }

  "handleError" should "fail with 404 status if NotFoundStatusException" in {
    //given
    val request = mock[Request[AnyContent]]
    val apiStatus = ApiStatus(404, "Entity is not found")
    val e = new NotFoundStatusException(apiStatus)

    //when
    val result = controller.handleError("Error during test", request, e)

    //then
    assertResult(result, 404, toJson(StatusResponse(apiStatus)))
  }

  it should "fail with 409 status if ConflictStatusException" in {
    //given
    val request = mock[Request[AnyContent]]
    val apiStatus = ApiStatus(409, "Entity already exists")
    val e = new ConflictStatusException(apiStatus)

    //when
    val result = controller.handleError("Error during test", request, e)

    //then
    assertResult(result, 409, toJson(StatusResponse(apiStatus)))
  }

  it should "fail with 400 status if BadRequestStatusException" in {
    //given
    val request = mock[Request[AnyContent]]
    val apiStatus = ApiStatus(400, "Name is blank")
    val e = new BadRequestStatusException(apiStatus)

    //when
    val result = controller.handleError("Error during test", request, e)

    //then
    assertResult(result, 400, toJson(StatusResponse(apiStatus)))
  }

  it should "fail with 500 status if InternalServerErrorStatusException" in {
    //given
    val request = mock[Request[AnyContent]]
    val apiStatus = ApiStatus(500, "Failed to remove entity")
    val e = new InternalServerErrorStatusException(apiStatus)

    //when
    val result = controller.handleError("Error during test", request, e)

    //then
    assertResult(result, 500, toJson(StatusResponse(apiStatus)))
  }

  it should "fail with 500 status if unknown Exception" in {
    //given
    val request = mock[Request[AnyContent]]
    val e = new Exception("test exception")

    //when
    val result = controller.handleError("Error during test", request, e)

    //then
    assertResult(result, 500, toJson(StatusResponse(ApiStatus(500, "Error during test",
      s"Request: $request\n\nError: ${stackTrace(e)}"))))

    verify(mockLogger).error(s"Error during test: $request", e)
  }

  private def assertResult(result: Result, expectedStatus: Int, expectedBody: JsValue): Assertion = {
    result.header.status shouldBe expectedStatus
    result.body.contentType shouldBe Some("application/json")
    result.body.consumeData.futureValue.utf8String shouldBe Json.stringify(expectedBody)
  }

  private def stackTrace(e: Exception): String = {
    val stackTrace = new StringWriter
    e.printStackTrace(new PrintWriter(stackTrace, true))
    stackTrace.toString
  }
}

object BaseApiControllerSpec {

  case class TestData(id: Int, name: String)

  object TestData {
    implicit val jsonFormat: Format[TestData] = Json.format[TestData]
  }

  case class TestResp(status: ApiStatus,
                      data: Option[TestData]) extends DataResponse[TestData]

  object TestResp {
    implicit val jsonFormat: Format[TestResp] = Json.format[TestResp]

    def apply(data: TestData): TestResp = TestResp(ApiStatus.Ok, Some(data))
  }
}
