package common

import org.sbtidea.SbtIdeaPlugin.ideaExcludeFolders
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys._
import scoverage.ScoverageSbtPlugin

object Common {

  val settings: Seq[Setting[_]] = Seq(
    organization := "org.scommons",
    scalaVersion := "2.12.2",
    scalacOptions ++= Seq(
      //"-Xcheckinit",
      "-Xfatal-warnings",
      "-Xlint:_",
      "-explaintypes",
      "-unchecked",
      "-deprecation",
      "-feature"
    ),
    //ivyScala := ivyScala.value map {
    //  _.copy(overrideScalaVersion = true)
    //},
    ideaExcludeFolders := List(
      ".idea"
    ),
    //when run tests with coverage: "sbt clean coverage test it:test coverageReport && sbt coverageAggregate"
    coverageMinimum := 80,
    coverageExcludedPackages := "scommons.client.test.raw",

    //use patched versions by now, to make scoverage work with scalajs-bundler
    libraryDependencies ++= {
      if (coverageEnabled.value) {
        Seq(
          Def.setting("org.scommons.patched" %%% "scalac-scoverage-runtime" % "1.4.0-SNAPSHOT").value,
          "org.scommons.patched" %% "scalac-scoverage-plugin" % "1.4.0-SNAPSHOT" % ScoverageSbtPlugin.ScoveragePluginConfig.name
        )
      }
      else Nil
    },
    libraryDependencies ~= (_.map(_.exclude("org.scoverage", "scalac-scoverage-runtime_2.12"))),
    libraryDependencies ~= (_.map(_.exclude("org.scoverage", "scalac-scoverage-runtime_sjs0.6_2.12"))),
    libraryDependencies ~= (_.map(_.exclude("org.scoverage", "scalac-scoverage-plugin_2.12"))),

    resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    //
    // publish/release related settings:
    //
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo := {
      if (isSnapshot.value)
        Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
      else
        Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
    },
    pomExtra := {
      <url>https://github.com/scommons/scommons</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:scommons/scommons.git</url>
          <connection>scm:git@github.com:scommons/scommons.git</connection>
        </scm>
        <developers>
          <developer>
            <id>viktorp</id>
            <name>Viktor Podzigun</name>
            <url>https://github.com/viktor-podzigun</url>
          </developer>
        </developers>
    },
    pomIncludeRepository := {
      _ => false
    }
  )
}
