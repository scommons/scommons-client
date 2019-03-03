package definitions

import common.TestLibs
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys._

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object ClientShowcase extends ScalaJsModule {

  override val id: String = "scommons-client-showcase"

  override val base: File = file("showcase")

  override def definition: Project = super.definition
    .settings(
      skip in publish := true,
      publish := ((): Unit),
      publishLocal := ((): Unit),
      publishM2 := ((): Unit),

      coverageEnabled := false,
      coverageExcludedPackages := ".*Css",

      scalaJSUseMainModuleInitializer := true,
      webpackBundlingMode := BundlingMode.LibraryOnly(),

      //dev
      webpackConfigFile in fastOptJS := Some(baseDirectory.value / "client.webpack.config.js"),
      //production
      webpackConfigFile in fullOptJS := Some(baseDirectory.value / "client.webpack.config.js"),
      //reload workflow and tests
      webpackConfigFile in Test := Some(baseDirectory.value / "test.webpack.config.js")
    )

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    ClientUi.definition
  )

  override def superRepoProjectsDependencies: Seq[(String, String, Option[String])] = Seq(
    ("scommons-react", "scommons-react-test-dom", Some("test"))
  )

  override def runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)

  override def testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scommonsReactTestDom.value
  ).map(_ % "test"))
}
