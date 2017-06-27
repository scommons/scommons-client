package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object TestLibs {

  lazy val scalajsDom = Def.setting("org.scala-js" %%% "scalajs-dom" % "0.9.2")

  lazy val scalaTest = Def.setting("org.scalatest" %%% "scalatest" % "3.0.0")

  lazy val scalaXml = Def.setting("scala-xml" %%% "scala-xml" % "1.0.7-SNAPSHOT")

  lazy val mockito = Def.setting("org.mockito" % "mockito-all" % "1.9.5")
}
