package definitions

import common.{Libs, TestLibs}
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys.coverageExcludedPackages

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object ClientUi extends ScalaJsModule {

  override val id: String = "scommons-client-ui"

  override val base: File = file("ui")

  override def definition: Project = super.definition
    .settings(
      description := "Common Scala.js, React.js web-client utilities and components",
      coverageExcludedPackages := ".*Css" +
        ";.*BaseStateAndRouteController" +
        ";scommons.client.ui.popup.WithAutoHide" + // causes "a dangling UndefinedParam", see https://github.com/scoverage/scalac-scoverage-plugin/issues/196
        ";scommons.client.ui.popup.raw" +
        ";scommons.client.ui.select.raw" +
        ";scommons.client.ui.HTML.InnerHTML",

      npmDependencies in Compile ++= Seq(
        "react-modal" -> "3.8.1",
        "react-select" -> "2.0.0"
      ),

      npmDevDependencies in Compile ++= Seq(
        "babel-loader" -> "8.0.5",
        "@babel/core" -> "7.4.0",
        "@babel/preset-env" -> "7.4.2",
        
        "css-loader" -> "2.1.1",
        "mini-css-extract-plugin" -> "0.12.0",
        "resolve-url-loader" -> "3.1.2",
        "url-loader" -> "4.1.1",
        "webpack-node-externals" -> "2.5.2",
        "webpack-merge" -> "4.2.1"
      )
    )

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Nil

  override val superRepoProjectsDependencies: Seq[(String, String, Option[String])] = Seq(
    ("scommons-api", "scommons-api-xhr", None),
    ("scommons-react", "scommons-react-core", None),
    ("scommons-react", "scommons-react-dom", None),
    ("scommons-react", "scommons-react-redux", None),
    
//    ("scommons-nodejs", "scommons-nodejs-test", Some("test")),
    ("scommons-react", "scommons-react-test", Some("test"))
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.scommonsApiXhr.value,
    Libs.scommonsReactCore.value,
    Libs.scommonsReactDom.value,
    Libs.scommonsReactRedux.value,

    Libs.sjsReactJsRouterDom.value,     // Optional. For react-router-dom facade
    Libs.sjsReactJsRouterRedux.value    // Optional. For react-router-redux facade
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scommonsNodejsTest.value,
    TestLibs.scommonsReactTest.value
  ).map(_ % "test"))
}
