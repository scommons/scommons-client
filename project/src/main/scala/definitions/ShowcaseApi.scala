package definitions

import common.{Common, Libs, TestLibs}
import org.sbtidea.SbtIdeaPlugin.ideaExcludeFolders
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.scalajs.sbtplugin.cross.CrossProject
import sbt.Keys._
import sbt._

object ShowcaseApi {

  val id: String = s"${Showcase.id}-api"

  def base: File = file(s"${Showcase.id}/api")

  private lazy val showcaseApi: CrossProject = crossProject.in(base)
    .dependsOn(ScommonsApi.scommonsApi)
    .settings(Common.settings: _*)
    .settings(
      sources in(Compile, doc) := Seq.empty,
      publishArtifact in(Compile, packageDoc) := false,
      ideaExcludeFolders ++= List(
        s"$base/jvm/target",
        s"$base/js/target",
        s"$base/shared/target"
      ),
      libraryDependencies ++= Seq(
        Libs.playJsonJs.value,
        TestLibs.scalaTest.value % "test"
      )
    ).jvmSettings(
      // Add JVM-specific settings here
      libraryDependencies ++= Seq(
        Libs.playWs.value,
        TestLibs.mockito.value % "test"
      )
    ).jsSettings(
      //Opt-in @ScalaJSDefined by default
      scalacOptions += "-P:scalajs:sjsDefinedByDefault",
      libraryDependencies ++= Seq(
        Libs.scalajsDom.value,
        TestLibs.scalaMock.value % "test"
      )
    )

  lazy val jvm: Project = showcaseApi.jvm

  lazy val js: Project = showcaseApi.js
}
