//import common.Common
import definitions._

lazy val scommons = (project in file("."))
  //.settings(Common.settings)
  //.configs(IntegrationTest)
  .aggregate(
  `scommons-client`,
  `scommons-client-test`,
  `scommons-showcase`
)

lazy val `scommons-client` = Client.definition

lazy val `scommons-client-test` = ClientTest.definition

lazy val `scommons-showcase` = Showcase.definition

lazy val `scommons-showcase-client` = Showcase.client

lazy val `scommons-showcase-server` = Showcase.server
