package scommons.components

import org.scalatest.{FlatSpec, Matchers}

class MyModuleSpec extends FlatSpec with Matchers {

  it should "test something" in {
    MyModule.multiplyByTwo(2) shouldBe 4
  }
}
