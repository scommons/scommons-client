//resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.7")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.20")
//use patched versions by now, to make scoverage work with scalajs-bundler
addSbtPlugin("org.scommons.patched" % "sbt-scalajs-bundler" % "0.9.0-SNAPSHOT")
//addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.9.0")
addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.9.0")
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.6")
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.1")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
