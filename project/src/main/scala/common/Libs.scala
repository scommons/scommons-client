package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Libs {

  private val playVer = "2.6.7"
  private val playWsVer = "1.1.3"

  private val sjsReactJsVer = "0.14.0"

  lazy val playJson = Def.setting("com.typesafe.play" %% "play-json" % playVer)
  lazy val play = Def.setting("com.typesafe.play" %% "play" % playVer)
  lazy val playWs = Def.setting("com.typesafe.play" %% "play-ahc-ws-standalone" % playWsVer)

  lazy val swaggerPlay = Def.setting("io.swagger" %% "swagger-play2" % "1.6.0")
  lazy val swaggerAnnotations = Def.setting("io.swagger" % "swagger-annotations" % "1.5.16")
  lazy val swaggerUi = Def.setting("org.webjars" % "swagger-ui" % "2.2.2")

  // Scala.js dependencies

  lazy val sjsReactJs = Def.setting("io.github.shogowada" %%% "scalajs-reactjs" % sjsReactJsVer)
  lazy val sjsReactJsRouterDom = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-dom" % sjsReactJsVer)
  lazy val sjsReactJsRouterRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-redux" % sjsReactJsVer)
  lazy val sjsReactJsRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-redux" % sjsReactJsVer)
  lazy val sjsReactJsReduxDevTools = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % sjsReactJsVer)

  lazy val playJsonJs = Def.setting("com.typesafe.play" %%% "play-json" % playVer)

  lazy val scalajsDom = Def.setting("org.scala-js" %%% "scalajs-dom" % "0.9.2")
}
