package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import common.Libs.akkaVersion
import sbt._

object TestLibs {

  private val scalaTestVersion = "3.0.1"

  lazy val mockito = Def.setting("org.mockito" % "mockito-all" % "1.9.5")

  lazy val scalaTestPlusPlay = Def.setting("org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2")
  lazy val akkaStreamTestKit = Def.setting("com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion)

  // Scala.js dependencies

  lazy val scalaTest = Def.setting("org.scalatest" %%% "scalatest" % scalaTestVersion)

  lazy val scalajsDom = Def.setting("org.scala-js" %%% "scalajs-dom" % "0.9.2")

  lazy val scalaXml = Def.setting("scala-xml" %%% "scala-xml" % "1.0.7-SNAPSHOT")

  lazy val scalaMock = Def.setting("org.scalamock" %%% "scalamock-scalatest-support" % "3.6.0")
}
