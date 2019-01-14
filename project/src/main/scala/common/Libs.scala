package common

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._
import scommons.sbtplugin.project.CommonLibs

object Libs extends CommonLibs {

  val scommonsReactVersion = "0.1.0-SNAPSHOT"
  private val scommonsApiVersion = "0.1.0-SNAPSHOT"
  private val sjsReactJsVer = "0.14.0"

  lazy val scommonsReactCore = Def.setting("org.scommons.react" %%% "scommons-react-core" % scommonsReactVersion)
  lazy val scommonsApiCore = Def.setting("org.scommons.api" %%% "scommons-api-core" % scommonsApiVersion)

  lazy val sjsReactJsRouterDom = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-dom" % sjsReactJsVer)
  lazy val sjsReactJsRouterRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-redux" % sjsReactJsVer)
  lazy val sjsReactJsRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-redux" % sjsReactJsVer)
  lazy val sjsReactJsReduxDevTools = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % sjsReactJsVer)
}
