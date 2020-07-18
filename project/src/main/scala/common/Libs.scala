package common

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._
import scommons.sbtplugin.project.CommonLibs

object Libs extends CommonLibs {

  val scommonsReactVersion = "0.3.0"
  private val scommonsApiVersion = "0.3.0"
  private val sjsReactJsVer = "0.14.0"

  lazy val scommonsApiXhr = Def.setting("org.scommons.api" %%% "scommons-api-xhr" % scommonsApiVersion)
  
  lazy val scommonsReactCore = Def.setting("org.scommons.react" %%% "scommons-react-core" % scommonsReactVersion)
  lazy val scommonsReactDom = Def.setting("org.scommons.react" %%% "scommons-react-dom" % scommonsReactVersion)
  lazy val scommonsReactRedux = Def.setting("org.scommons.react" %%% "scommons-react-redux" % scommonsReactVersion)

  lazy val sjsReactJsRouterDom = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-dom" % sjsReactJsVer)
  lazy val sjsReactJsRouterRedux = Def.setting("io.github.shogowada" %%% "scalajs-reactjs-router-redux" % sjsReactJsVer)
}
