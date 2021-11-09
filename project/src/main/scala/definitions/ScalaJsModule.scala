package definitions

import org.scalajs.sbtplugin.ScalaJSPlugin
import sbt.Keys._
import sbt._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._
import scommons.sbtplugin.ScommonsPlugin.autoImport._
import scommons.sbtplugin.project.CommonClientModule

trait ScalaJsModule extends ClientModule {

  override def definition: Project = {
    super.definition
      .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
      .settings(CommonClientModule.settings: _*)
      .settings(
        scommonsRequireWebpackInTest := true,
        webpackConfigFile in Test := Some(baseDirectory.value / "test.webpack.config.js")
      )
  }
}
