package scommons.client.controller

import scommons.react.test.TestSpec

class PathParamsSpec extends TestSpec {

  it should "extract int from the given location path when extractInt" in {
    //given
    val prefix = "/some-path"
    val idRegex = s"$prefix/(\\d+)".r

    //when & then
    PathParams("/").extractInt(idRegex) shouldBe None
    PathParams("/test").extractInt(idRegex) shouldBe None
    PathParams(s"/test/$prefix/1").extractInt(idRegex) shouldBe None
    PathParams(s"$prefix/test").extractInt(idRegex) shouldBe None
    PathParams(s"$prefix/1").extractInt(idRegex) shouldBe Some(1)
    PathParams(s"$prefix/123").extractInt(idRegex) shouldBe Some(123)
    PathParams(s"$prefix/123?test").extractInt(idRegex) shouldBe Some(123)
    PathParams(s"$prefix/123/test").extractInt(idRegex) shouldBe Some(123)
    PathParams(s"$prefix/123$prefix/456").extractInt(idRegex) shouldBe Some(123)
  }
  
  it should "extract int only if exact match when extractInt" in {
    //given
    val prefix = "/some-path"
    val idRegex = s"$prefix/(\\d+)".r

    //when & then
    PathParams("/").extractInt(idRegex, exact = true) shouldBe None
    PathParams("/test").extractInt(idRegex, exact = true) shouldBe None
    PathParams(s"/test/$prefix/1").extractInt(idRegex, exact = true) shouldBe None
    PathParams(s"$prefix/test").extractInt(idRegex, exact = true) shouldBe None
    PathParams(s"$prefix/1").extractInt(idRegex, exact = true) shouldBe Some(1)
    PathParams(s"$prefix/123").extractInt(idRegex, exact = true) shouldBe Some(123)
    PathParams(s"$prefix/123?test").extractInt(idRegex, exact = true) shouldBe None
    PathParams(s"$prefix/123/test").extractInt(idRegex, exact = true) shouldBe None
    PathParams(s"$prefix/123$prefix/456").extractInt(idRegex, exact = true) shouldBe None
  }
}
