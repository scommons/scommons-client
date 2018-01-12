package definitions

import common.Common
import sbt.Keys._
import sbt._

object Showcase {

  val id: String = "scommons-showcase"

  lazy val apiJVM: Project = ShowcaseApi.jvm
  lazy val apiJS: Project = ShowcaseApi.js

  lazy val client: Project = ShowcaseClient.definition

  lazy val server: Project = ShowcaseServer.definition

  lazy val definition: Project = Project(id = id, base = file(id))
    .settings(Common.settings)
    .settings(
      skip in publish := true,
      publish := (),
      publishM2 := ()
    )
    .aggregate(
      apiJVM,
      apiJS,
      client,
      server
    )
}
