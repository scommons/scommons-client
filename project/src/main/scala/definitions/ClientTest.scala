package definitions

import common.{Libs, TestLibs}
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys.coverageExcludedPackages

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object ClientTest extends ScalaJsModule {

  override val id: String = "scommons-client-test"

  override def definition: Project = super.definition
    .settings(
      description := "Common Scala.js, React.js testing utilities and components",
      coverageExcludedPackages := "scommons.client.test.raw",

      npmDependencies in Compile ++= Seq(
        "react-addons-test-utils" -> "15.6.0",
        "react-test-renderer" -> "15.6.1"
      )
    )

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Nil

  override val superRepoProjectsDependencies: Seq[(String, String, Option[String])] = Seq(
    ("scommons-react", "scommons-react-core", None)
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.scommonsReactCore.value,
    Libs.scalajsDom.value,
    TestLibs.scalaTestJs.value,
    TestLibs.scalaMockJs.value
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)
}
