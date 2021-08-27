package definitions

import org.scalajs.jsenv.nodejs.NodeJSEnv
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
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
        webpackConfigFile in Test := Some(baseDirectory.value / "test.webpack.config.js"),

        // required for node.js >= v12.12.0
        // see:
        //   https://github.com/nodejs/node/pull/29919
        jsEnv in Test := new NodeJSEnv(NodeJSEnv.Config().withArgs(List("--enable-source-maps"))),
        scalaJSLinkerConfig in Test ~= {
          _.withSourceMap(true)
        }
      )
  }
}
