package definitions

import common.{Libs, TestLibs}
import sbt._

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object ClientTest extends ScalaJsModule {

  override val id: String = "scommons-client-test"

  override def definition: Project = {
    super.definition
      .settings(
        npmDependencies in Compile ++= Seq(
          "react-addons-test-utils" -> "15.6.0",
          "react-test-renderer" -> "15.6.1"
        )
      )
  }

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Nil

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.sjsReactJs.value,
    TestLibs.scalaTest.value,
    TestLibs.scalajsDom.value,
    TestLibs.scalaXml.value
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)
}
