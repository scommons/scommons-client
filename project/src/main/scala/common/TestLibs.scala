package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object TestLibs {

  private val scalaTestVersion = "3.0.1"
  private val scalaMockVersion = "3.6.0"

  lazy val scalaTestJs = Def.setting("org.scalatest" %%% "scalatest" % scalaTestVersion)
  lazy val scalaMockJs = Def.setting("org.scalamock" %%% "scalamock-scalatest-support" % scalaMockVersion)
}
