//resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.20")

//use patched versions by now, to make scoverage work with scalajs-bundler
addSbtPlugin(("org.scommons.patched" % "sbt-scalajs-bundler" % "0.9.0-SNAPSHOT").force())
//dependencyOverrides ++= Set(
//  "org.scommons.patched" % "sbt-scalajs-bundler" % "0.9.0-SNAPSHOT"
//)

//addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.9.0")

addSbtPlugin(("org.scommons" % "sbt-scommons-plugin" % "0.1.0-SNAPSHOT").changing())

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.2.4")
