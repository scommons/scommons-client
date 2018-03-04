package scommons.client.util

import org.scalatest.{FlatSpec, Matchers}

class IdentitySpec extends FlatSpec with Matchers {

  it should "perform identity equality" in {
    //given
    val key1 = Identity(new Array(0))
    val key2 = Identity(new Array(0))
    key1.obj shouldBe key2.obj

    //when & then
    key1 should be theSameInstanceAs key1
    key1 should not (be theSameInstanceAs key2)
    key1 shouldBe key1
    key1 should not be key2
  }

  it should "produce identity hashCode" in {
    //given
    val key1 = Identity(new Array(0))
    val key2 = Identity(new Array(0))
    key1.obj shouldBe key2.obj

    //when & then
    key1.hashCode shouldBe key1.hashCode
    key1.hashCode should not be key2.hashCode
    key1.hashCode shouldBe System.identityHashCode(key1.obj)
    key2.hashCode shouldBe System.identityHashCode(key2.obj)
  }
}
