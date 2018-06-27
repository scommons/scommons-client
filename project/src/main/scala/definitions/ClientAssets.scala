package definitions

import com.typesafe.sbt.web.SbtWeb
import sbt.Keys._
import sbt._

object ClientAssets extends ClientModule {

  override val id: String = "scommons-client-assets"

  override def definition: Project = super.definition
    .enablePlugins(SbtWeb)
    .settings(
      description := "Web assets for scommons Client UI components"
    )

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Nil

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)
}
