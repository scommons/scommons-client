package definitions

import common.Common
import sbt.Keys._
import sbt._

trait BasicModule extends ProjectDef {

  private val buildInfoGenerator = (sourceManaged in Compile, version, name) map { (d, v, n) =>
    val file = d / "metadata/Info.scala"
    IO.write(file,
      """package metadata
        |object Info {
        |  val version = "%s"
        |  val name = "%s"
        |}
        |""".stripMargin.format(v, n))
    Seq(file)
  }

  def definition: Project = Project(id = id, base = base)
    .dependsOn(internalDependencies: _*)
    .settings(Common.settings: _*)
    .settings(
      libraryDependencies ++= (runtimeDependencies.value ++ testDependencies.value),
      sources in(Compile, doc) := Seq.empty,
      sourceGenerators in Compile <+= buildInfoGenerator,
      publishArtifact in(Compile, packageDoc) := false
    )
}
