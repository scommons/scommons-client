package scommons.client.util

import scommons.nodejs.test.TestSpec

class BrowsePathSpec extends TestSpec {

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
  
  it should "not construct BrowsePath if its empty" in {
    //when
    val e = the[IllegalArgumentException] thrownBy {
      BrowsePath("")
    }
    
    //then
    e.getMessage should include("BrowsePath should not be empty!")
  }

  it should "not construct BrowsePath if it doesn't start with '/'" in {
    //given
    val path = "test"

    //when
    val e = the[IllegalArgumentException] thrownBy {
      BrowsePath(path)
    }

    //then
    e.getMessage should include(s"BrowsePath should start with '/', path: $path")
  }

  it should "not construct BrowsePath if it doesn't start with prefix" in {
    //given
    val path = "/test"
    val prefix = "/test2"

    //when
    val e = the[IllegalArgumentException] thrownBy {
      BrowsePath(path, prefix)
    }

    //then
    e.getMessage should include(s"BrowsePath should start with prefix, path: $path, prefix: $prefix")
  }

  it should "not copy BrowsePath if value doesn't start with prefix" in {
    //given
    val prefix = "/test"
    val value = "/tes2"
    val data = BrowsePath(prefix)

    //when
    val e = the[IllegalArgumentException] thrownBy {
      data.copy(value = value)
    }

    //then
    e.getMessage should include(s"BrowsePath should start with prefix, path: $value, prefix: $prefix")
  }

  it should "match url path accordingly" in {
    //when & then
    BrowsePath("/").matches("/path") shouldBe false
    BrowsePath("/path").matches("/") shouldBe false
    BrowsePath("/path").matches("/test") shouldBe false
    BrowsePath("/path").matches("/path/1") shouldBe false
    BrowsePath("/path/1").matches("/path") shouldBe false
    BrowsePath("/path").matches("/path") shouldBe true
    //when & then
    BrowsePath("/path", exact = false).matches("/") shouldBe false
    BrowsePath("/path", exact = false).matches("/test") shouldBe false
    BrowsePath("/path", exact = false).matches("/path") shouldBe true
    BrowsePath("/path", exact = false).matches("/path/1/2") shouldBe true
    //when & then
    BrowsePath("/path", "/path").matches("/") shouldBe false
    BrowsePath("/path", "/path").matches("/test") shouldBe false
    BrowsePath("/path/1", "/path").matches("/test") shouldBe false
    BrowsePath("/path", "/path").matches("/path/1") shouldBe true
    BrowsePath("/path/1", "/path").matches("/path") shouldBe true
    BrowsePath("/path/1", "/path").matches("/path/1/2") shouldBe true
    BrowsePath("/path/1/2", "/path").matches("/path/1/2") shouldBe true
  }
}
