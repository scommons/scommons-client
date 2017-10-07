package definitions

import com.typesafe.sbt.web.SbtWeb
import common.Libs
import sbt.Keys._
import sbt._

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object Client extends ScalaJsModule {

  override val id: String = "scommons-client"

  override def definition: Project = {
    super.definition
      .enablePlugins(SbtWeb)
      .settings(
        webpackConfigFile in Test := Some(baseDirectory.value / "common.webpack.config.js"),

        npmDevDependencies in Compile ++= Seq(
          "css-loader" -> "0.23.1",
          "extract-text-webpack-plugin" -> "1.0.1",
          "resolve-url-loader" -> "2.0.2",
          "file-loader" -> "0.11.2",
          "style-loader" -> "0.13.1",
          "webpack-merge" -> "4.1.0"
        )
      )
  }

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    ClientTest.definition % "test"
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.sjsReactJs.value,              // For react facade
    Libs.sjsReactJsRouterDom.value,     // Optional. For react-router-dom facade
    Libs.sjsReactJsRouterRedux.value,   // Optional. For react-router-redux facade
    Libs.sjsReactJsRedux.value,         // Optional. For react-redux facade
    Libs.sjsReactJsReduxDevTools.value  // Optional. For redux-devtools facade
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)
}
