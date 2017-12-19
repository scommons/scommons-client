package definitions

import common.{Libs, TestLibs}
import sbt._

object ScommonsPlay extends BasicModule {

  override val id: String = "scommons-play"

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    ScommonsApi.jvm
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    Libs.play.value,
    Libs.swaggerPlay.value,
    Libs.swaggerAnnotations.value,
    Libs.swaggerUi.value
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scalaTestPlusPlay.value,
    TestLibs.mockito.value
  ).map(_ % "test"))
}
