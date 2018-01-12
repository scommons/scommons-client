import common.Common
import definitions._

lazy val scommons = (project in file("."))
  .settings(Common.settings)
  .settings(
    skip in publish := true,
    publish := (),
    publishM2 := ()
  )
  .aggregate(
  `scommons-api-jvm`,
  `scommons-api-js`,
  `scommons-client`,
  `scommons-client-test`,
  `scommons-play-ws-api-client`,
  `scommons-play`,
  `scommons-showcase`
)

lazy val `scommons-api-jvm` = ScommonsApi.jvm
lazy val `scommons-api-js` = ScommonsApi.js

lazy val `scommons-client` = ScommonsClient.definition
lazy val `scommons-client-test` = ScommonsClientTest.definition

lazy val `scommons-play-ws-api-client` = ScommonsPlayWsApiClient.definition
lazy val `scommons-play` = ScommonsPlay.definition

lazy val `scommons-showcase` = Showcase.definition
lazy val `scommons-showcase-api-jvm` = Showcase.apiJVM
lazy val `scommons-showcase-api-js` = Showcase.apiJS
lazy val `scommons-showcase-client` = Showcase.client
lazy val `scommons-showcase-server` = Showcase.server
