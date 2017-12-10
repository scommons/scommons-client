package scommons.showcase

import org.scalatest.DoNotDiscover

@DoNotDiscover
class FailingApiIntegrationSpec extends BaseShowcaseIntegrationSpec {

  it should "fail with timeout error" in {
    //when
    val futureResult = callFailingTimedout()

    //then
    eventually {
      futureResult.value.get.failed.get.getMessage should include ("Request timeout")
    }
  }

  it should "fail with example error" in {
    //when
    val futureResult = callFailingFailed()

    //then
    eventually {
      futureResult.value.get.get.status.error.get should include ("Error while processing request")
      futureResult.value.get.get.status.details.get should include ("Example error")
    }
  }
}
