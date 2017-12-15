package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import common.Libs.akkaVersion
import sbt._

object TestLibs {

  private val scalaTestVersion = "3.0.1"
  private val scalaMockVersion = "3.6.0"

  lazy val scalaTest = Def.setting("org.scalatest" %% "scalatest" % scalaTestVersion)
  lazy val scalaMock = Def.setting("org.scalamock" %% "scalamock-scalatest-support" % scalaMockVersion)

  lazy val mockito = Def.setting("org.mockito" % "mockito-all" % "1.9.5")

  lazy val scalaTestPlusPlay = Def.setting("org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2")
  lazy val akkaStreamTestKit = Def.setting("com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion)

  // Scala.js dependencies

  lazy val scalaTestJs = Def.setting("org.scalatest" %%% "scalatest" % scalaTestVersion)
  lazy val scalaMockJs = Def.setting("org.scalamock" %%% "scalamock-scalatest-support" % scalaMockVersion)

  lazy val scalaXmlJs = Def.setting("scala-xml" %%% "scala-xml" % "1.0.7-SNAPSHOT")
}
