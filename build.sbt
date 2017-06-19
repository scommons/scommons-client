import common.Common
import definitions._

lazy val scommons = (project in file("."))
  //.settings(Common.settings)
  //.configs(IntegrationTest)
  .aggregate(
  `scommons-client`,
  `scommons-client-test`
)

lazy val `scommons-client` = Client.definition

lazy val `scommons-client-test` = ClientTest.definition
