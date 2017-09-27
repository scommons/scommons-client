package definitions

import com.typesafe.sbt.web.SbtWeb
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._
import webscalajs.ScalaJSWeb

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin

object ShowcaseClient extends ScalaJsModule {

  override val id: String = s"${Showcase.id}-client"

  override def base: File = file(s"${Showcase.id}/client")

  override def definition: Project = {
    super.definition
      .enablePlugins(ScalaJSBundlerPlugin, ScalaJSWeb, SbtWeb)
      .settings(
        scalaJSUseMainModuleInitializer := true
      )
  }

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    Client.definition
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)
}
