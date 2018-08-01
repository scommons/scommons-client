package scommons.client.util

import scommons.client.test.TestSpec
import scommons.client.util.PathParamsExtractors.extractId

class PathParamsExtractorsSpec extends TestSpec {

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
    extractId(idRegex, s"$prefix/123") shouldBe Some(123)
    extractId(idRegex, s"$prefix/123?test") shouldBe Some(123)
    extractId(idRegex, s"$prefix/123/test") shouldBe Some(123)
    extractId(idRegex, s"$prefix/123$prefix/456") shouldBe Some(123)
  }
}
