package definitions

import common.{Libs, TestLibs}
import sbt._

object ScommonsPlayWsApiClient extends BasicModule {

  override val id: String = "scommons-play-ws-api-client"

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    ScommonsApi.jvm
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.playWs.value
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scalaTest.value,
    TestLibs.mockito.value
  ).map(_ % "test"))
}
