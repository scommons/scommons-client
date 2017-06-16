package definitions

import common.{Libs, TestLibs}
import org.sbtidea.SbtIdeaPlugin.ideaExcludeFolders
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object Client extends BasicModule {

  override val id: String = "scommons-client"

  override def definition: Project = {
    super.definition
      .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
      .settings(
        scalaJSModuleKind := ModuleKind.CommonJSModule,
        //Opt-in @ScalaJSDefined by default
        scalacOptions += "-P:scalajs:sjsDefinedByDefault",
        //scalaJSUseMainModuleInitializer := true,
//        npmDependencies in Compile ++= Seq(
//          "react" -> "15.3.2",
//          "react-dom" -> "15.3.2",
//          "react-redux" -> "4.4.5",
//          "react-router" -> "2.6.0",
//          "redux" -> "3.5.2"
//        ),
        npmDependencies in Test ++= Seq(
          "react-addons-test-utils" -> "15.6.0",
          "react-test-renderer" -> "15.6.1"
        ),
        requiresDOM in Test := true,
        //(version in webpack) := "2.3.2",
//        (webpack in(Compile, fastOptJS)) := Seq(),
//        (webpack in(Compile, fullOptJS)) := Seq(),
        enableReloadWorkflow := true,
        emitSourceMaps := false,
        ideaExcludeFolders ++= List(
          s"$id/build",
          s"$id/node_modules",
          s"$id/target"
        )
      )
  }

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Nil

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.sjsReactJs.value,              // For react facade
    Libs.sjsReactJsRouterDom.value,     // Optional. For react-router-dom facade
    Libs.sjsReactJsRouterRedux.value,   // Optional. For react-router-redux facade
    Libs.sjsReactJsRedux.value,         // Optional. For react-redux facade
    Libs.sjsReactJsReduxDevTools.value  // Optional. For redux-devtools facade
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scalaTest.value
  ) map (_  % "test"))
}
