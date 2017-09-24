package definitions

import com.typesafe.sbt.web.SbtWeb
import com.typesafe.sbt.web.SbtWeb.autoImport._
import common.Libs
import play.sbt.{PlayLayoutPlugin, PlayScala}
import sbt._
import scoverage.ScoverageKeys.coverageExcludedPackages
import webscalajs.WebScalaJS.autoImport._

import scalajsbundler.sbtplugin.WebScalaJSBundlerPlugin.autoImport._
import scalajsbundler.sbtplugin.{NpmAssets, WebScalaJSBundlerPlugin}

object ShowcaseServer extends BasicModule {

  override val id: String = s"${Showcase.id}-server"

  override def base: File = file(s"${Showcase.id}/server")

  override def definition: Project = {
    super.definition
      .enablePlugins(PlayScala, WebScalaJSBundlerPlugin, SbtWeb)
      .disablePlugins(PlayLayoutPlugin)
      .settings(
        coverageExcludedPackages := "<empty>;Reverse.*",
        scalaJSProjects := Seq(Showcase.client),
        pipelineStages in Assets := Seq(scalaJSPipeline),
        //pipelineStages := Seq(digest, gzip),
        // Expose as sbt-web assets some files retrieved from the NPM packages of the `client` project
        npmAssets ++= NpmAssets.ofProject(Showcase.client) { modules => (modules / "font-awesome").*** }.value
      )
  }

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Nil

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.play.value,
    Libs.playJson.value,
    Libs.slf4jApi.value,
    Libs.logback.value
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)
}
