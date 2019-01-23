package scommons.client.ui.page

import org.scalatest.{FlatSpec, Matchers}

class PaginationAlignmentSpec extends FlatSpec with Matchers {

  it should "return style according to pagination alignment type" in {
    //when & then
    PaginationAlignment.Left.style shouldBe "pagination"
    PaginationAlignment.Centered.style shouldBe "pagination pagination-centered"
    PaginationAlignment.Right.style shouldBe "pagination pagination-right"
  }
}
