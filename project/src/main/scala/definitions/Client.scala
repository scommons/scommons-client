package definitions

import common.TestLibs
import org.sbtidea.SbtIdeaPlugin.ideaExcludeFolders
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._

object Client extends BasicModule {

  override val id: String = "scommons-client"

  override def definition: Project = {
    super.definition
      .enablePlugins(ScalaJSPlugin)
      .settings(
        scalaJSModuleKind := ModuleKind.CommonJSModule,
        //Opt-in @ScalaJSDefined by default
        scalacOptions += "-P:scalajs:sjsDefinedByDefault",
        scalaJSUseMainModuleInitializer := true,
        ideaExcludeFolders ++= List(
          s"$id/build",
          s"$id/node_modules",
          s"$id/target"
        )
      )
  }

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Nil

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scalaTest.value
  ) map (_  % "test"))
}
