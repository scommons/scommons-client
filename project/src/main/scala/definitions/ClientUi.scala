package definitions

import common.Libs
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys.coverageExcludedPackages

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object ClientUi extends ScalaJsModule {

  override val id: String = "scommons-client-ui"

  override def definition: Project = super.definition
    .settings(
      description := "Common Scala.js, React.js web-client utilities and components",
      coverageExcludedPackages := ".*Css" +
        ";.*BaseStateController" +
        ";scommons.client.ui.popup.raw" +
        ";scommons.client.ui.HTML.InnerHTML",

      webpackConfigFile in Test := Some(
        baseDirectory.value / "src" / "main" / "resources" / "scommons.webpack.config.js"
      ),

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

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    ClientCore.definition,
    ClientTest.definition % "test"
  )

  override val superRepoProjectsDependencies: Seq[(String, String, Option[String])] = Seq(
    ("scommons-api", "scommons-api-coreJS", None)
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.scommonsApiCore.value,

    Libs.sjsReactJs.value,              // For react facade
    Libs.sjsReactJsRouterDom.value,     // Optional. For react-router-dom facade
    Libs.sjsReactJsRouterRedux.value,   // Optional. For react-router-redux facade
    Libs.sjsReactJsRedux.value,         // Optional. For react-redux facade
    Libs.sjsReactJsReduxDevTools.value  // Optional. For redux-devtools facade
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)
}
