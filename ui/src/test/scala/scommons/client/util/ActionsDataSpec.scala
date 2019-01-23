package scommons.client.util

import org.scalatest.{FlatSpec, Matchers}

class ActionsDataSpec extends FlatSpec with Matchers {

  it should "return empty actions when ActionsData.empty" in {
    //when
    val result = ActionsData.empty

    //then
    result.enabledCommands shouldBe Set.empty[String]
    result.onCommand(identity) shouldBe PartialFunction.empty
    result.focusedCommand shouldBe None
  }
}
