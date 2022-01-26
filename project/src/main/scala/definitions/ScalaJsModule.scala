package definitions

import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._
import scommons.sbtplugin.ScommonsPlugin.autoImport._
import scommons.sbtplugin.project.CommonClientModule
import scoverage.ScoverageKeys.{coverageEnabled, coverageScalacPluginVersion}

trait ScalaJsModule extends ClientModule {

  override def definition: Project = {
    super.definition
      .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
      .settings(CommonClientModule.settings: _*)
      .settings(
        scommonsRequireWebpackInTest := true,
        webpackConfigFile in Test := Some(baseDirectory.value / "test.webpack.config.js"),

        //TODO: remove these temporal fixes for Scala.js 1.1+ and scoverage
        coverageScalacPluginVersion := {
          val current = coverageScalacPluginVersion.value
          if (scalaJSVersion.startsWith("0.6")) current
          else "1.4.2" //the only version that supports Scala.js 1.1+
        },
        libraryDependencies ~= { modules =>
          if (scalaJSVersion.startsWith("0.6")) modules
          else modules.filter(_.organization != "org.scoverage")
        },
        libraryDependencies ++= {
          if (coverageEnabled.value) {
            if (scalaJSVersion.startsWith("0.6")) Nil
            else Seq(
              "org.scoverage" %% "scalac-scoverage-runtime_sjs1" % coverageScalacPluginVersion.value,
              "org.scoverage" %% "scalac-scoverage-plugin" % coverageScalacPluginVersion.value % "scoveragePlugin"
            )
          }
          else Nil
        }
      )
  }
}
