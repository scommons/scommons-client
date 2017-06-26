package definitions

import common.{Libs, TestLibs}
import sbt._

object Client extends ScalaJsModule {

  override val id: String = "scommons-client"

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    ClientTest.definition % "test"
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.sjsReactJs.value,              // For react facade
    Libs.sjsReactJsRouterDom.value,     // Optional. For react-router-dom facade
    Libs.sjsReactJsRouterRedux.value,   // Optional. For react-router-redux facade
    Libs.sjsReactJsRedux.value,         // Optional. For react-redux facade
    Libs.sjsReactJsReduxDevTools.value  // Optional. For redux-devtools facade
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scalajsDom.value,
    TestLibs.scalaTest.value
  ) map (_  % "test"))
}
