package scommons.client.ui.page

import org.scalatest.{FlatSpec, Matchers}

class PaginationPanelPropsSpec extends FlatSpec with Matchers {

  it should "fail if totalPages is less than 1" in {
    def assertFailedProps(totalPages: Int): Unit = {
      val selectedPage = 1
      val e = the[IllegalArgumentException] thrownBy {
        PaginationPanelProps(totalPages, selectedPage)
      }

      e.getMessage should include (
        s"totalPages($totalPages) should be greater than or equal 1"
      )
    }

    //when & then
    assertFailedProps(-1)
    assertFailedProps(0)
  }

  it should "fail if selectedPage is out of range" in {
    def assertFailedProps(totalPages: Int, selectedPage: Int): Unit = {
      val e = the[IllegalArgumentException] thrownBy {
        PaginationPanelProps(totalPages, selectedPage)
      }

      e.getMessage should include (
        s"selectedPage($selectedPage) should be between 1 and $totalPages"
      )
    }

    //when & then
    assertFailedProps(1, -1)
    assertFailedProps(1, 0)
    assertFailedProps(1, 2)
    assertFailedProps(5, 0)
    assertFailedProps(5, 6)
  }

  it should "return props if selectedPage is within range" in {
    //when & then
    PaginationPanelProps(1).selectedPage shouldBe 1
    PaginationPanelProps(2).selectedPage shouldBe 1
    PaginationPanelProps(2, 2).selectedPage shouldBe 2
    PaginationPanelProps(5).selectedPage shouldBe 1
    PaginationPanelProps(5, 3).selectedPage shouldBe 3
    PaginationPanelProps(5, 5).selectedPage shouldBe 5
  }
}
