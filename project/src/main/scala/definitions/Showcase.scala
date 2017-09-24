package definitions

import sbt._

object Showcase {

  val id: String = "scommons-showcase"

  lazy val client: Project = ShowcaseClient.definition

  lazy val server: Project = ShowcaseServer.definition

  lazy val definition: Project = Project(id = id, base = file(id))
    .aggregate(
      client,
      server
    )
}
