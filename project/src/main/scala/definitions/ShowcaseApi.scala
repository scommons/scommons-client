package definitions

import common.Common
import org.sbtidea.SbtIdeaPlugin.ideaExcludeFolders
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.scalajs.sbtplugin.cross.CrossProject
import sbt.Keys._
import sbt._

object ShowcaseApi {

  val id: String = s"${Showcase.id}-api"

  def base: File = file(s"${Showcase.id}/api")

  private lazy val `scommons-showcase-api`: CrossProject = crossProject.crossType(CrossType.Pure).in(base)
    .dependsOn(ScommonsApi.`scommons-api`)
    .settings(Common.settings: _*)
    .settings(
      skip in publish := true,
      publish := (),
      publishM2 := ()
    )
    .settings(
      ideaExcludeFolders ++= List(
        s"$base/.jvm/target",
        s"$base/.js/target"
      )
    ).jvmSettings(
      // Add JVM-specific settings here
    ).jsSettings(
      //Opt-in @ScalaJSDefined by default
      scalacOptions += "-P:scalajs:sjsDefinedByDefault"
    )

  lazy val jvm: Project = `scommons-showcase-api`.jvm

  lazy val js: Project = `scommons-showcase-api`.js
}
