package scommons.client.ui.page

import scommons.client.test.TestSpec
import scommons.client.ui.page.PaginationPanel.getSelectedRange

class PaginationPanelSpec extends TestSpec {

  it should "return selected range when getSelectedRange" in {
    //when & then
    getSelectedRange(1, 1) shouldBe (1 to 1)
    getSelectedRange(1, 2) shouldBe (1 to 2)
    getSelectedRange(1, 3) shouldBe (1 to 3)
    getSelectedRange(1, 4) shouldBe (1 to 4)
    getSelectedRange(1, 5) shouldBe (1 to 5)

    getSelectedRange(1, 5) shouldBe (1 to 5)
    getSelectedRange(2, 5) shouldBe (1 to 5)
    getSelectedRange(3, 5) shouldBe (1 to 5)
    getSelectedRange(4, 5) shouldBe (1 to 5)
    getSelectedRange(5, 5) shouldBe (1 to 5)

    getSelectedRange(1, 6) shouldBe (1 to 5)
    getSelectedRange(2, 6) shouldBe (1 to 5)
    getSelectedRange(3, 6) shouldBe (1 to 5)
    getSelectedRange(4, 6) shouldBe (2 to 6)
    getSelectedRange(5, 6) shouldBe (2 to 6)
    getSelectedRange(6, 6) shouldBe (2 to 6)

    getSelectedRange(1, 10) shouldBe (1 to 5)
    getSelectedRange(2, 10) shouldBe (1 to 5)
    getSelectedRange(3, 10) shouldBe (1 to 5)
    getSelectedRange(4, 10) shouldBe (2 to 6)
    getSelectedRange(5, 10) shouldBe (3 to 7)
    getSelectedRange(6, 10) shouldBe (4 to 8)
    getSelectedRange(7, 10) shouldBe (5 to 9)
    getSelectedRange(8, 10) shouldBe (6 to 10)
    getSelectedRange(9, 10) shouldBe (6 to 10)
    getSelectedRange(10, 10) shouldBe (6 to 10)
  }
}
