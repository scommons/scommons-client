package definitions

import common.{Libs, TestLibs}
import sbt.Keys._
import sbt._

object ScommonsPlayWsApiClient extends BasicModule {

  override val id: String = "scommons-play-ws-api-client"

  override def definition: Project = {
    super.definition
      .settings(
        description := "Common Scala play-ws ApiHttpClient implementation"
      )
  }

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
