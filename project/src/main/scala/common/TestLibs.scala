package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object TestLibs {

  lazy val scalaTest = Def.setting("org.scalatest" %%% "scalatest" % "3.0.0")

  lazy val mockito = Def.setting("org.mockito" % "mockito-all" % "1.9.5")
}
