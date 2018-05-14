
[![Build Status](https://travis-ci.org/scommons/scommons-client.svg?branch=master)](https://travis-ci.org/scommons/scommons-client)
[![Coverage Status](https://coveralls.io/repos/github/scommons/scommons-client/badge.svg?branch=master)](https://coveralls.io/github/scommons/scommons-client?branch=master)
[![Scala.js](https://www.scala-js.org/assets/badges/scalajs-0.6.17.svg)](https://www.scala-js.org)

## Scala Commons
Scala and Scala.js common utilities and components.


### Showcase/Demo UI

To see the showcase/demo UI with all the components live use the following link:

[https://scommons.org/scommons-showcase/](https://scommons.org/scommons-showcase/)

### How to add it to your project

Current version is under active development, but you already can try it:
```scala
val scommonsApiVer = "0.1.0-SNAPSHOT"
val scommonsServiceVer = "0.1.0-SNAPSHOT"
val scommonsClientVer = "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  // shared
  "org.scommons.api" %%% "scommons-api-core" % scommonsApiVer,
  "org.scommons.api" %%% "scommons-api-joda-time" % scommonsApiVer,

  // server/jvm only
  "org.scommons.api" %% "scommons-api-play-ws" % scommonsApiVer,
  "org.scommons.service" %% "scommons-service-play" % scommonsServiceVer,

  // client/js only
  "org.scommons.client" %%% "scommons-client-ui" % scommonsClientVer,
  "org.scommons.client" %%% "scommons-client-test" % scommonsClientVer % "test"
)
```

Latest `SNAPSHOT` version is published to [Sonatype Repo](https://oss.sonatype.org/content/repositories/snapshots/org/scommons/), just make sure you added
the proper dependency resolver to your `build.sbt` settings:
```scala
resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
```

### How to Build

To build and run all the tests use the following command:
```bash
sbt clean test
```

### How to Run Showcase/Demo server locally

Please, see the README.md in [scommons-showcase](https://github.com/scommons/scommons-showcase) project page.


### Documentation

You can find documentation [here](https://scommons.org/scommons-client)
