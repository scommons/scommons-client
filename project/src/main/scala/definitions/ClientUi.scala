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

      Compile / npmDependencies ++= Seq(
        "react-modal" -> "3.14.3",
        "react-select" -> "3.2.0"
      ),

      Compile / npmDevDependencies ++= Seq(
        "css-loader" -> "6.7.2",
        "mini-css-extract-plugin" -> "2.6.1",
        "webpack-node-externals" -> "3.0.0",
        "webpack-merge" -> "5.8.0"
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
    Libs.sjsReactJsRouterDom.value
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scommonsNodejsTest.value,
    TestLibs.scommonsReactTest.value
  ).map(_ % "test"))
}
