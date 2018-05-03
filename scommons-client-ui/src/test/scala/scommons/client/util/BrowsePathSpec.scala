package scommons.client.util

import org.scalatest.{FlatSpec, Matchers}

class BrowsePathSpec extends FlatSpec with Matchers {

  it should "perform value equality" in {
    //given
    val path1 = BrowsePath("/path")
    val path2 = BrowsePath("/path")

    //when & then
    path1 shouldBe path2
    path1 should not (be theSameInstanceAs path2.asInstanceOf[Object])
  }

  it should "return value when toString" in {
    //given
    val path = BrowsePath("/path")

    //when & then
    path.toString shouldBe path.value
  }
}
