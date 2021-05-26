package definitions

import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import scommons.sbtplugin.project.CommonClientModule

import scalajsbundler._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

trait ScalaJsModule extends ClientModule {

  override def definition: Project = {
    super.definition
      .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
      .settings(CommonClientModule.settings: _*)
      .settings(
        requireJsDomEnv in Test := false,

        //reload workflow and tests
        webpackConfigFile in Test := Some(baseDirectory.value / "test.webpack.config.js"),
        version in webpack := "4.29.0",

        fastOptJS in Test := {
          val sjsOutput = (fastOptJS in Test).value

          val logger = streams.value.log
          val targetDir = (npmUpdate in Test).value
          val sjsOutputName = sjsOutput.data.name.stripSuffix(".js")
          val webpackOutput = targetDir / s"$sjsOutputName-webpack-out.js"

          val customWebpackConfigFile = (webpackConfigFile in Test).value
          val nodeArgs = (webpackNodeArgs in Test).value

          logger.info("Executing webpack...")
          val loader = sjsOutput.data

          customWebpackConfigFile match {
            case Some(configFile) =>
              val customConfigFileCopy = Webpack.copyCustomWebpackConfigFiles(targetDir, webpackResources.value.get)(configFile)
              Webpack.run(nodeArgs: _*)("--mode", "development", "--config", customConfigFileCopy.getAbsolutePath, loader.absolutePath, "--output", webpackOutput.absolutePath)(targetDir, logger)
            case None =>
              Webpack.run(nodeArgs: _*)("--mode", "development", loader.absolutePath, "--output", webpackOutput.absolutePath)(targetDir, logger)
          }

          Attributed(webpackOutput)(sjsOutput.metadata)
        }
      )
  }
}
