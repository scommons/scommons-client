
[![Build Status](https://travis-ci.org/scommons/scommons.svg?branch=master)](https://travis-ci.org/scommons/scommons)
[![Coverage Status](https://coveralls.io/repos/github/scommons/scommons/badge.svg?branch=master)](https://coveralls.io/github/scommons/scommons?branch=master)

## Scala Commons
Scala and Scala.js common utilities and components.


### Showcase/Demo UI

To see the showcase/demo UI with all the components live use the following link:

[http://scommons.org/scommons-showcase/](http://scommons.org/scommons-showcase/)

### How to add it to your project

Current version is under active development, but you already can try it:
```scala
val scommonsVer = "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  // shared
  "org.scommons" %%% "scommons-api" % scommonsVer,

  // server only
  "org.scommons" %% "scommons-play" % scommonsVer,
  "org.scommons" %% "scommons-play-ws-api-client" % scommonsVer,

  // client only
  "org.scommons" %%% "scommons-client" % scommonsVer,
  "org.scommons" %%% "scommons-client-test" % scommonsVer % "test"
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


## Documentation

//TODO
