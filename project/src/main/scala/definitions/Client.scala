package definitions

import common.TestLibs
import org.sbtidea.SbtIdeaPlugin.ideaExcludeFolders
import org.scalajs.sbtplugin.ScalaJSPlugin
import sbt._

object Client extends BasicModule {

  override val id: String = "scommons-client"

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Nil

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scalaTest.value
  ) map (_  % "test"))

  override def definition: Project = {
    super.definition.enablePlugins(ScalaJSPlugin)
      .settings(
        ideaExcludeFolders ++= List(
          s"$id/build",
          s"$id/node_modules",
          s"$id/target"
        )
      )
  }
}
