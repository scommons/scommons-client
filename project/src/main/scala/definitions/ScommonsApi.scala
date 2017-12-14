package definitions

import common.{Common, Libs, TestLibs}
import org.sbtidea.SbtIdeaPlugin.ideaExcludeFolders
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.scalajs.sbtplugin.cross.CrossProject
import sbt.Keys._
import sbt._

object ScommonsApi {

  val id: String = "scommons-api"

  def base: File = file(id)

  lazy val scommonsApi: CrossProject = crossProject.in(base)
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
        TestLibs.scalaTestJs.value % "test",
        TestLibs.scalaMockJs.value % "test"
      )
    ).jvmSettings(
      // Add JVM-specific settings here
    ).jsSettings(
      //Opt-in @ScalaJSDefined by default
      scalacOptions += "-P:scalajs:sjsDefinedByDefault",
      libraryDependencies ++= Seq(
        Libs.scalajsDom.value
      )
    )

  lazy val jvm: Project = scommonsApi.jvm

  lazy val js: Project = scommonsApi.js
}
