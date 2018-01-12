package definitions

import com.typesafe.sbt.web.SbtWeb
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._
import sbt.Keys._
import webscalajs.ScalaJSWeb

import scalajsbundler.BundlingMode
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object ShowcaseClient extends ScalaJsModule {

  override val id: String = s"${Showcase.id}-client"

  override def base: File = file(s"${Showcase.id}/client")

  override def definition: Project = {
    super.definition
      .enablePlugins(ScalaJSBundlerPlugin, ScalaJSWeb, SbtWeb)
      .settings(
        skip in publish := true,
        publish := (),
        publishM2 := ()
      )
      .settings(
        scalaJSUseMainModuleInitializer := true,
        webpackBundlingMode := BundlingMode.LibraryOnly(),

        npmUpdate in Compile := {
          copyWebpackResources(
            (npmUpdate in Compile).value,
            (fullClasspath in Compile).value
          )
        },

        npmUpdate in Test := {
          copyWebpackResources(
            (npmUpdate in Test).value,
            (fullClasspath in Test).value
          )
        },

        //dev
        webpackConfigFile in fastOptJS := Some(baseDirectory.value / "dev.webpack.config.js"),
        //production
        webpackConfigFile in fullOptJS := Some(baseDirectory.value / "prod.webpack.config.js"),
        //reload workflow and tests
        webpackConfigFile in Test := Some(baseDirectory.value / "common.webpack.config.js")
      )
  }

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    Showcase.apiJS,
    ScommonsClient.definition
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)


  private def copyWebpackResources(targetDir: File, cp: Def.Classpath): File = {
    ResourcesHelper.extractFromClasspath(targetDir, cp,
      "*.css" || "*.ico" || "*.png" || "*.jpg" || "*.jpeg" || "*.gif")
    targetDir
  }
}
