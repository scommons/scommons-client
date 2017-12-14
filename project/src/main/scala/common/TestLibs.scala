package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import common.Libs.akkaVersion
import sbt._

object TestLibs {

  private val scalaTestVersion = "3.0.1"

  lazy val scalaTest = Def.setting("org.scalatest" %% "scalatest" % scalaTestVersion)

  lazy val mockito = Def.setting("org.mockito" % "mockito-all" % "1.9.5")

  lazy val scalaTestPlusPlay = Def.setting("org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2")
  lazy val akkaStreamTestKit = Def.setting("com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion)

  // Scala.js dependencies

  lazy val scalaTestJs = Def.setting("org.scalatest" %%% "scalatest" % scalaTestVersion)

  lazy val scalaXml = Def.setting("scala-xml" %%% "scala-xml" % "1.0.7-SNAPSHOT")

  lazy val scalaMockJs = Def.setting("org.scalamock" %%% "scalamock-scalatest-support" % "3.6.0")
}
