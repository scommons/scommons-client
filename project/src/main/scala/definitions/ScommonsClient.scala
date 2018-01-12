package definitions

import common.{Libs, TestLibs}
import sbt.Keys._
import sbt._

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object ScommonsClient extends ScalaJsModule {

  override val id: String = "scommons-client"

  override def definition: Project = {
    super.definition
      .settings(
        description := "Common Scala.js, React.js web-client utilities and components",

        webpackConfigFile in Test := Some(baseDirectory.value / "test.webpack.config.js"),

        npmUpdate in Compile := {
          copyWebpackResources(
            (npmUpdate in Compile).value,
            (fullClasspath in Compile).value
          )
        },

        npmUpdate in Test := {
          copyWebpackResources(
            (npmUpdate in Test).value,
            (fullClasspath in Test).value
          )
        },

        npmDependencies in Compile ++= Seq(
          "react-modal" -> "3.1.2"
        ),

        npmDevDependencies in Compile ++= Seq(
          "css-loader" -> "0.23.1",
          "extract-text-webpack-plugin" -> "1.0.1",
          "resolve-url-loader" -> "2.0.2",
          "url-loader" -> "0.5.8",
          "file-loader" -> "0.11.2",
          "style-loader" -> "0.13.1",
          "webpack-merge" -> "4.1.0"
        )
      )
  }

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    ScommonsApi.js,
    ScommonsClientTest.definition % "test"
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.sjsReactJs.value,              // For react facade
    Libs.sjsReactJsRouterDom.value,     // Optional. For react-router-dom facade
    Libs.sjsReactJsRouterRedux.value,   // Optional. For react-router-redux facade
    Libs.sjsReactJsRedux.value,         // Optional. For react-redux facade
    Libs.sjsReactJsReduxDevTools.value  // Optional. For redux-devtools facade
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scalaMockJs.value
  ).map(_ % "test"))

  private def copyWebpackResources(targetDir: File, cp: Def.Classpath): File = {
    ResourcesHelper.extractFromClasspath(targetDir, cp,
      "*.css" || "*.ico" || "*.png" || "*.jpg" || "*.jpeg" || "*.gif")
    targetDir
  }
}
