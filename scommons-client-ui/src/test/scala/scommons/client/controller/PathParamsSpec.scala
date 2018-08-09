package scommons.client.controller

import scommons.client.controller.PathParams.extractId
import scommons.client.test.TestSpec

class PathParamsSpec extends TestSpec {

  it should "extract id from the given location path when extractId" in {
    //given
    val prefix = "/some-path"
    val idRegex = s"$prefix/(\\d+)".r

    //when & then
    extractId(idRegex, "") shouldBe None
    extractId(idRegex, "/") shouldBe None
    extractId(idRegex, "/test") shouldBe None
    extractId(idRegex, s"/test/$prefix/1") shouldBe None
    extractId(idRegex, s"$prefix/test") shouldBe None
    extractId(idRegex, s"$prefix/1") shouldBe Some(1)
    extractId(idRegex, s"$prefix/123") shouldBe Some(123)
    extractId(idRegex, s"$prefix/123?test") shouldBe Some(123)
    extractId(idRegex, s"$prefix/123/test") shouldBe Some(123)
    extractId(idRegex, s"$prefix/123$prefix/456") shouldBe Some(123)
  }
  
  it should "extract id only if exact match when extractId" in {
    //given
    val prefix = "/some-path"
    val idRegex = s"$prefix/(\\d+)".r

    //when & then
    extractId(idRegex, "", exact = true) shouldBe None
    extractId(idRegex, "/", exact = true) shouldBe None
    extractId(idRegex, "/test", exact = true) shouldBe None
    extractId(idRegex, s"/test/$prefix/1", exact = true) shouldBe None
    extractId(idRegex, s"$prefix/test", exact = true) shouldBe None
    extractId(idRegex, s"$prefix/1", exact = true) shouldBe Some(1)
    extractId(idRegex, s"$prefix/123", exact = true) shouldBe Some(123)
    extractId(idRegex, s"$prefix/123?test", exact = true) shouldBe None
    extractId(idRegex, s"$prefix/123/test", exact = true) shouldBe None
    extractId(idRegex, s"$prefix/123$prefix/456", exact = true) shouldBe None
  }
}
