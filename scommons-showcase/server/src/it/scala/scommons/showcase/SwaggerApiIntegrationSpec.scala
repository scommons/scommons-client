package scommons.showcase

import org.scalatest.DoNotDiscover
import org.scalatestplus.play.WsScalaTestClient

@DoNotDiscover
class SwaggerApiIntegrationSpec extends BaseShowcaseIntegrationSpec with WsScalaTestClient {

  it should "return api-docs json" in {
    //given
    val request = wsUrl("/scommons-showcase/api-docs")

    //when
    val response = request.get.futureValue

    //then
    response.status shouldBe 200
    response.contentType shouldBe "application/json"

    val json = response.json
    (json \ "swagger").as[String] shouldBe "2.0"
    (json \ "info" \ "description").as[String] shouldBe "REST API for scommons Showcase Demo Server"
    (json \ "info" \ "version").as[String] shouldBe "1.0"
    (json \ "basePath").as[String] shouldBe "/scommons-showcase"

    (json \ "paths" \ "/repos/{id}").isDefined shouldBe true
    (json \ "paths" \ "/repos").isDefined shouldBe true
    (json \ "paths" \ "/failing/timedout").isDefined shouldBe true

    (json \ "definitions" \ "ApiStatus").isDefined shouldBe true
    (json \ "definitions" \ "RepoData").isDefined shouldBe true
    (json \ "definitions" \ "RepoResp").isDefined shouldBe true
  }

  ignore should "redirect to swagger ui html page when /swagger.html" in {
    //given
    val request = wsUrl("/scommons-showcase/swagger.html")

    //when
    val response = request.get.futureValue

    //then
    response.status shouldBe 200
    //response.contentType shouldBe "application/json"
  }

  ignore should "return swagger ui html page from web-jar assets" in {
    //given
    val request = wsUrl("/scommons-showcase/assets/lib/swagger-ui/index.html")

    //when
    val response = request.get.futureValue

    //then
    response.status shouldBe 200
    //response.contentType shouldBe "application/json"
  }
}
