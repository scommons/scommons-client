package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Libs {

  private val sjsReactJsVer = "0.14.0"

  lazy val sjsReactJs = Def.setting("io.github.shogowada" %%% "scalajs-reactjs" % sjsReactJsVer)
  lazy val sjsReactJsRouterDom = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-dom" % sjsReactJsVer)
  lazy val sjsReactJsRouterRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-redux" % sjsReactJsVer)
  lazy val sjsReactJsRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-redux" % sjsReactJsVer)
  lazy val sjsReactJsReduxDevTools = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % sjsReactJsVer)

  lazy val playJson = "com.typesafe.play" %% "play-json" % "2.5.3"
  lazy val play = "com.typesafe.play" %% "play" % "2.5.3"

//  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
//  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.1.7"
//  lazy val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.12"
//  lazy val log4jToSlf4j = "org.apache.logging.log4j" % "log4j-to-slf4j" % "2.2"
//  lazy val jclOverSlf4j = "org.slf4j" % "jcl-over-slf4j" % "1.7.12"
}
