
[![Build Status](https://travis-ci.org/scommons/scommons-client.svg?branch=master)](https://travis-ci.org/scommons/scommons-client)
[![Coverage Status](https://coveralls.io/repos/github/scommons/scommons-client/badge.svg?branch=master)](https://coveralls.io/github/scommons/scommons-client?branch=master)
[![scala-index](https://index.scala-lang.org/scommons/scommons-client/scommons-client-ui/latest.svg)](https://index.scala-lang.org/scommons/scommons-client/scommons-client-ui)
[![Scala.js](https://www.scala-js.org/assets/badges/scalajs-0.6.17.svg)](https://www.scala-js.org)

## Scala Commons Client
Common Scala.js, React.js web-client utilities and components.


### Showcase/Demo UI

To see the showcase/demo UI with all the components live use the following link:

[https://scommons.org/scommons-client/showcase/](https://scommons.org/scommons-client/showcase/)

### How to add it to your project

```scala
val scommonsApiVer = "1.0.0-SNAPSHOT"
val scommonsReactVer = "1.0.0-SNAPSHOT"
val scommonsClientVer = "1.0.0-SNAPSHOT"
val scommonsServiceVer = "1.0.0-SNAPSHOT"

libraryDependencies ++= Seq(
  // shared
  "org.scommons.api" %%% "scommons-api-core" % scommonsApiVer,
  "org.scommons.api" %%% "scommons-api-joda-time" % scommonsApiVer,

  // client/js only
  "org.scommons.api" %%% "scommons-api-xhr" % scommonsApiVer,
  "org.scommons.client" %%% "scommons-client-ui" % scommonsClientVer,
  "org.scommons.react" %%% "scommons-react-test-dom" % scommonsReactVer % "test",

  // server/jvm only
  "org.scommons.service" %% "scommons-service-play" % scommonsServiceVer,
  "org.scommons.api" %% "scommons-api-play-ws" % scommonsApiVer % "test"
)
```

Latest `SNAPSHOT` version is published to [Sonatype Repo](https://oss.sonatype.org/content/repositories/snapshots/org/scommons/), just make sure you added
the proper dependency resolver to your `build.sbt` settings:
```scala
resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
```

### How to use it

* [TablePanelDemo](showcase/src/main/scala/scommons/client/showcase/table/TablePanelDemo.scala) => [tests](showcase/src/test/scala/scommons/client/showcase/table/TablePanelDemoSpec.scala)
  * [SimpleTablePanel](showcase/src/main/scala/scommons/client/showcase/table/SimpleTablePanel.scala) => [tests](showcase/src/test/scala/scommons/client/showcase/table/SimpleTablePanelSpec.scala)
  * [CustomTablePanel](showcase/src/main/scala/scommons/client/showcase/table/CustomTablePanel.scala) => [tests](showcase/src/test/scala/scommons/client/showcase/table/CustomTablePanelSpec.scala)

### How to Build

To build and run all the tests use the following command:
```bash
sbt clean test
```

### How to Run Showcase/Demo locally

Please, see the README.md in [showcase](https://github.com/scommons/scommons-client/tree/master/showcase) sub-project page.


## Documentation

You can find more documentation [here](https://scommons.org/scommons-client)
