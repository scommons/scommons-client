package common

import org.sbtidea.SbtIdeaPlugin.ideaExcludeFolders
import sbt.Keys._
import sbt._

object Common {

  val settings: Seq[Setting[_]] = Seq(
    organization := "com.github.viktor-podzigun",
    //maintainer := "viktor.podzigun@gmail.com",
    version := sys.props.getOrElse("version", default = "0"),
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq(
      //"-Xcheckinit",
      "-Xfatal-warnings",
      "-Xlint:_",
      "-explaintypes",
      "-unchecked",
      "-deprecation",
      "-feature"
      //"-language:implicitConversions",
      //"-language:higherKinds"
    ),
    //ivyScala := ivyScala.value map {
    //  _.copy(overrideScalaVersion = true)
    //},
    ideaExcludeFolders := List(
      ".idea"
    ),
    resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
  )
}
