package definitions

import com.typesafe.sbt.digest.Import.digest
import com.typesafe.sbt.gzip.Import.gzip
import com.typesafe.sbt.web.PathMapping
import com.typesafe.sbt.web.SbtWeb.autoImport._
import common.{Libs, TestLibs}
import play.sbt.{PlayLayoutPlugin, PlayScala}
import sbt._
import scoverage.ScoverageKeys.coverageExcludedPackages
import webscalajs.WebScalaJS.autoImport._

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport.npmUpdate
import scalajsbundler.sbtplugin.WebScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.WebScalaJSBundlerPlugin.autoImport._

object ShowcaseServer extends BasicModule {

  override val id: String = s"${Showcase.id}-server"

  override def base: File = file(s"${Showcase.id}/server")

  override def definition: Project = {
    super.definition
      .enablePlugins(PlayScala, WebScalaJSBundlerPlugin)
      .disablePlugins(PlayLayoutPlugin)
      .settings(
        coverageExcludedPackages := "<empty>;Reverse.*",
        scalaJSProjects := Seq(Showcase.client),
        pipelineStages in Assets := Seq(scalaJSPipeline),
        pipelineStages := Seq(digest, gzip),
        // Expose as sbt-web assets some webpack build files of the `client` project
        npmAssets ++= WebpackAssets.ofProject(Showcase.client) { build => (build / "styles").*** }.value
      )
  }

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    Showcase.apiJVM,
    Showcase.client
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.play.value,
    Libs.playJson.value,
    Libs.slf4jApi.value,
    Libs.logback.value
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scalaTestPlusPlay.value
  ).map(_ % "test"))
}

object WebpackAssets {

  def ofProject(project: ProjectReference)(assets: File => PathFinder): Def.Initialize[Task[Seq[PathMapping]]] =
    Def.task {
      val build = (npmUpdate in (project, Compile)).value
      assets(build).pair(relativeTo(build))
    }
}
