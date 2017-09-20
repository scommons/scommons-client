package definitions

import common.Libs
import play.sbt.{PlayLayoutPlugin, PlayScala}
import sbt._
import scoverage.ScoverageKeys.coverageExcludedPackages

object Showcase extends BasicModule {

  override val id: String = "scommons-showcase"

  override def definition: Project = {
    super.definition
      .enablePlugins(PlayScala)
      .disablePlugins(PlayLayoutPlugin)
      .settings(
        coverageExcludedPackages := "<empty>;Reverse.*"
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
