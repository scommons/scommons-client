import common.Common
import definitions._

lazy val scommons = (project in file("."))
  .settings(Common.settings)
  .settings(
    skip in publish := true,
    publish := (),
    publishM2 := ()
  )
  .settings(
    ideaExcludeFolders ++= List(
      "docs/_site",
      "target"
    )
  )
  .aggregate(
  `scommons-api-jvm`,
  `scommons-api-js`,
  `scommons-api-client-play-ws`,
  `scommons-client`,
  `scommons-client-test`,
  `scommons-play`
)

lazy val `scommons-api-jvm` = ScommonsApi.jvm
lazy val `scommons-api-js` = ScommonsApi.js
lazy val `scommons-api-client-play-ws` = ScommonsApiClientPlayWs.definition

lazy val `scommons-client` = ScommonsClient.definition
lazy val `scommons-client-test` = ScommonsClientTest.definition

lazy val `scommons-play` = ScommonsPlay.definition
