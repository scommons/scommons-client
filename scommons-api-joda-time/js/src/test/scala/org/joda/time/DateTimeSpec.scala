package org.joda.time

import org.scalatest.{FlatSpec, Matchers}

class DateTimeSpec extends FlatSpec with Matchers {

  it should "fail if DateTime value is not ISO8601 time formatted string" in {
    //given
    val isoString = "2018-03-03T13:43:01.234+01"

    //when
    val e = the[IllegalArgumentException] thrownBy {
      DateTime(isoString)
    }

    //then
    e.getMessage should include (
      s"datetime string '$isoString' is not in ISO8601 format"
    )
  }

  it should "create DateTime with valid iso string" in {
    //given
    val isoString = "2018-03-03T13:43:01.234Z"

    //when & then
    DateTime(isoString).toString shouldBe isoString
  }

  it should "perform value equality" in {
    //given
    val d1 = DateTime("2018-03-03T13:43:01.234Z")
    val d2 = DateTime("2018-03-03T13:43:01.234Z")

    //when & then
    d1 shouldBe d2
    (d1 == d2) shouldBe true
    d1 should not (be theSameInstanceAs d2)
  }
}
