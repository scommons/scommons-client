package scommons.client.util

import scommons.nodejs.test.TestSpec

class ActionsDataSpec extends TestSpec {

  it should "return empty actions when ActionsData.empty" in {
    //when
    val result = ActionsData.empty

    //then
    result.enabledCommands shouldBe Set.empty[String]
    result.onCommand(identity) shouldBe PartialFunction.empty
    result.focusedCommand shouldBe None
  }
}
