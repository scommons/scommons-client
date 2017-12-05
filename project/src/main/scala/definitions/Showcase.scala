package definitions

import sbt._

object Showcase {

  val id: String = "scommons-showcase"

  lazy val apiJVM: Project = ShowcaseApi.jvm
  lazy val apiJS: Project = ShowcaseApi.js

  lazy val client: Project = ShowcaseClient.definition

  lazy val server: Project = ShowcaseServer.definition

  lazy val definition: Project = Project(id = id, base = file(id))
    .aggregate(
      apiJVM,
      apiJS,
      client,
      server
    )
}
