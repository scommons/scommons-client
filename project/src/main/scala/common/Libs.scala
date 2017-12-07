package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Libs {

  private val playVer = "2.6.5"
  private val playWsVer = "1.1.3"
  private val sjsReactJsVer = "0.14.0"

  lazy val playJson = Def.setting("com.typesafe.play" %% "play-json" % playVer)
  lazy val play = Def.setting("com.typesafe.play" %% "play" % playVer)
  lazy val playWs = Def.setting("com.typesafe.play" %% "play-ahc-ws-standalone" % playWsVer)
  lazy val playWsJson = Def.setting("com.typesafe.play" %% "play-ws-standalone-json" % playWsVer)
  lazy val scaldiPlay = Def.setting("org.scaldi" %% "scaldi-play" % "0.5.17")

  //lazy val scalaLogging = Def.setting("com.typesafe.scala-logging" %% "scala-logging" % "3.5.0")
  lazy val logback = Def.setting("ch.qos.logback" % "logback-classic" % "1.1.7")
  lazy val slf4jApi = Def.setting("org.slf4j" % "slf4j-api" % "1.7.12")
  lazy val log4jToSlf4j = Def.setting("org.apache.logging.log4j" % "log4j-to-slf4j" % "2.2")
  lazy val jclOverSlf4j = Def.setting("org.slf4j" % "jcl-over-slf4j" % "1.7.12")

  // Scala.js dependencies

  lazy val sjsReactJs = Def.setting("io.github.shogowada" %%% "scalajs-reactjs" % sjsReactJsVer)
  lazy val sjsReactJsRouterDom = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-dom" % sjsReactJsVer)
  lazy val sjsReactJsRouterRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-redux" % sjsReactJsVer)
  lazy val sjsReactJsRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-redux" % sjsReactJsVer)
  lazy val sjsReactJsReduxDevTools = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % sjsReactJsVer)

  lazy val playJsonJs = Def.setting("com.typesafe.play" %%% "play-json" % playVer)
}
