package scommons.client

import org.scalatest.{FlatSpec, Matchers}

class TutorialAppSpec extends FlatSpec with Matchers {

  it should "test something" in {
    TutorialApp.doSomething() shouldBe 4
  }

  it should "test main" in {
    TutorialApp.main()
  }
}
