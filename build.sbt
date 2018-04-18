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
    ideaExcludeFolders += s"${baseDirectory.value}/docs/_site"
  )
  .aggregate(
  `scommons-api-jvm`,
  `scommons-api-js`,
  `scommons-api-joda-time-jvm`,
  `scommons-api-joda-time-js`,
  `scommons-api-play-ws`,
  `scommons-client-assets`,
  `scommons-client-core`,
  `scommons-client-ui`,
  `scommons-client-test`,
  `scommons-play`
)

lazy val `scommons-api-jvm` = ScommonsApi.jvm
lazy val `scommons-api-js` = ScommonsApi.js
lazy val `scommons-api-joda-time-jvm` = ScommonsApiJodaTime.jvm
lazy val `scommons-api-joda-time-js` = ScommonsApiJodaTime.js
lazy val `scommons-api-play-ws` = ScommonsApiPlayWs.definition

lazy val `scommons-client-assets` = ScommonsClientAssets.definition
lazy val `scommons-client-core` = ScommonsClientCore.definition
lazy val `scommons-client-ui` = ScommonsClientUi.definition
lazy val `scommons-client-test` = ScommonsClientTest.definition

lazy val `scommons-play` = ScommonsPlay.definition
