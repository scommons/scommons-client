import definitions._
import scommons.sbtplugin.project.CommonModule
import scommons.sbtplugin.project.CommonModule.ideExcludedDirectories

lazy val `scommons-client` = (project in file("."))
  .settings(CommonModule.settings: _*)
  .settings(ClientModule.settings: _*)
  .settings(
    skip in publish := true,
    publish := (),
    publishM2 := ()
  )
  .settings(
    ideExcludedDirectories += baseDirectory.value / "docs" / "_site"
  )
  .aggregate(
  `scommons-client-assets`,
  `scommons-client-ui`
)

lazy val `scommons-client-assets` = ClientAssets.definition
lazy val `scommons-client-ui` = ClientUi.definition
