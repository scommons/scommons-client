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
      s"docs/_site"
    )
  )
  .aggregate(
  `scommons-api-jvm`,
  `scommons-api-js`,
  `scommons-client`,
  `scommons-client-test`,
  `scommons-play-ws-api-client`,
  `scommons-play`
)

lazy val `scommons-api-jvm` = ScommonsApi.jvm
lazy val `scommons-api-js` = ScommonsApi.js

lazy val `scommons-client` = ScommonsClient.definition
lazy val `scommons-client-test` = ScommonsClientTest.definition

lazy val `scommons-play-ws-api-client` = ScommonsPlayWsApiClient.definition
lazy val `scommons-play` = ScommonsPlay.definition
