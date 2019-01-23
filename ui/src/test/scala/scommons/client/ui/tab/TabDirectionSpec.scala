package scommons.client.ui.tab

import org.scalatest.{FlatSpec, Matchers}

class TabDirectionSpec extends FlatSpec with Matchers {

  it should "return style according to tab direction type" in {
    //when & then
    TabDirection.Top.style shouldBe "tabbable"
    TabDirection.Bottom.style shouldBe "tabbable tabs-below"
    TabDirection.Left.style shouldBe "tabbable tabs-left"
    TabDirection.Right.style shouldBe "tabbable tabs-right"
  }
}
