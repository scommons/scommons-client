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
        scalaJSUseMainModuleInitializer := true,
        webpackBundlingMode := BundlingMode.LibraryOnly(),
        webpackConfigFile := Some(baseDirectory.value / "showcase.webpack.config.js"),
        npmDevDependencies in Compile ++= Seq(
          "css-loader" -> "0.23.1",
          "extract-text-webpack-plugin" -> "1.0.1",
          "resolve-url-loader" -> "2.0.2",
          "file-loader" -> "0.11.1",
          "style-loader" -> "0.13.1",
          "webpack-merge" -> "4.1.0"
        )
      )
  }

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    Client.definition
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)
}
