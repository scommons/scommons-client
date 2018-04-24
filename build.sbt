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
  `scommons-client-assets`,
  `scommons-client-core`,
  `scommons-client-ui`,
  `scommons-client-test`,
  `scommons-play`
)

lazy val `scommons-client-assets` = ScommonsClientAssets.definition
lazy val `scommons-client-core` = ScommonsClientCore.definition
lazy val `scommons-client-ui` = ScommonsClientUi.definition
lazy val `scommons-client-test` = ScommonsClientTest.definition

lazy val `scommons-play` = ScommonsPlay.definition
