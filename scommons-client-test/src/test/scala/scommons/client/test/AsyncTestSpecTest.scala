package scommons.client.test

class AsyncTestSpecTest extends AsyncTestSpec {

  it should "run successfully after several retries when eventually" in {
    //given
    val value = 5
    var retries = 3

    //when & then
    eventually {
      retries -= 1
      if (retries >= 0) value shouldBe 1
      else value shouldBe 5
    }
  }

  it should "fail if not succeeded within maxRetries when eventually" in {
    //given
    val value = 5
    var retries = 10

    //when & then
    eventually {
      retries -= 1
      if (retries >= 0) value shouldBe 1
      else value shouldBe 5
    }.failed.map { result =>
      result.getMessage should include ("5 was not equal to 1")
    }
  }
}
