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
      publish / skip := true,
      publish := ((): Unit),
      publishLocal := ((): Unit),
      publishM2 := ((): Unit),

      coverageEnabled := false,
      coverageExcludedPackages := ".*Css",

      scalaJSUseMainModuleInitializer := true,
      webpackBundlingMode := BundlingMode.LibraryOnly(),

      //dev
      fastOptJS / webpackConfigFile := Some(baseDirectory.value / "client.webpack.config.js"),
      //production
      fullOptJS / webpackConfigFile := Some(baseDirectory.value / "client.webpack.config.js")
    )

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    ClientUi.definition
  )

  override def superRepoProjectsDependencies: Seq[(String, String, Option[String])] = Seq(
    ("scommons-react", "scommons-react-test", Some("test"))
  )

  override def runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)

  override def testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scommonsReactTest.value
  ).map(_ % "test"))
}
