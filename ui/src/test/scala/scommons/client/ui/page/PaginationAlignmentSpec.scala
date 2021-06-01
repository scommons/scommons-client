package scommons.client.ui.page

import scommons.nodejs.test.TestSpec

class PaginationAlignmentSpec extends TestSpec {

  it should "return style according to pagination alignment type" in {
    //when & then
    PaginationAlignment.Left.style shouldBe "pagination"
    PaginationAlignment.Centered.style shouldBe "pagination pagination-centered"
    PaginationAlignment.Right.style shouldBe "pagination pagination-right"
  }
}
