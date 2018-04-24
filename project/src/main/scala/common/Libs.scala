package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Libs {

  private val scommonsApiVersion = "0.1.0-SNAPSHOT"
  private val playVer = "2.6.7"
  private val sjsReactJsVer = "0.14.0"

  lazy val play = Def.setting("com.typesafe.play" %% "play" % playVer)

  lazy val swaggerPlay = Def.setting("io.swagger" %% "swagger-play2" % "1.6.0")
  lazy val swaggerAnnotations = Def.setting("io.swagger" % "swagger-annotations" % "1.5.16")
  lazy val swaggerUi = Def.setting("org.webjars" % "swagger-ui" % "2.2.2")

  // Scala.js dependencies

  lazy val scalajsDom = Def.setting("org.scala-js" %%% "scalajs-dom" % "0.9.2")
  lazy val scommonsApiCore = Def.setting("org.scommons.api" %%% "scommons-api-core" % scommonsApiVersion)

  lazy val sjsReactJs = Def.setting("io.github.shogowada" %%% "scalajs-reactjs" % sjsReactJsVer)
  lazy val sjsReactJsRouterDom = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-dom" % sjsReactJsVer)
  lazy val sjsReactJsRouterRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-redux" % sjsReactJsVer)
  lazy val sjsReactJsRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-redux" % sjsReactJsVer)
  lazy val sjsReactJsReduxDevTools = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % sjsReactJsVer)
}
