package scommons.showcase

import akka.actor.ActorSystem
import akka.testkit.SocketUtil
import org.scalatest.{Suites, TestSuite}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import scaldi.Module
import scaldi.play.ScaldiApplicationBuilder
import scommons.api.http.ws.WsApiHttpClient
import scommons.showcase.api.ShowcaseApiClient

class AllShowcaseIntegrationTests extends Suites(
  new RepoApiIntegrationSpec,
  new FailingApiIntegrationSpec
) with TestSuite
  with GuiceOneServerPerSuite {

  override lazy val port: Int = {
    val (_, serverPort) = SocketUtil.temporaryServerHostnameAndPort()
    serverPort
  }

  implicit override lazy val app: Application = {
    val showcaseApiUrl = s"http://localhost:$port/scommons-showcase"
    println(s"showcaseApiUrl: $showcaseApiUrl")

    val apiClient = new ShowcaseApiClient(new WsApiHttpClient(showcaseApiUrl)(ActorSystem("ShowcaseApiWsClient")))

    new ScaldiApplicationBuilder(modules = List(new Module {
      //test-only
      bind[ShowcaseApiClient] to apiClient
    })).configure(
      // custom configuration
      //"quill.db.port" -> postgresPort
    ).build()
  }
}
