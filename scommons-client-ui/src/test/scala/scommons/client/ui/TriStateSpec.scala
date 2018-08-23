package scommons.client.ui

import scommons.client.test.TestSpec
import scommons.client.ui.TriState._

class TriStateSpec extends TestSpec {

  it should "convert Boolean to TriState" in {
    //when & then
    TriState(true) shouldBe Selected
    TriState(false) shouldBe Deselected
  }
  
  it should "convert TriState to Boolean" in {
    //when & then
    TriState.isSelected(Selected) shouldBe true
    TriState.isSelected(Indeterminate) shouldBe false
    TriState.isSelected(Deselected) shouldBe false
  }
  
  it should "return next TriState" in {
    //when & then
    Selected.next shouldBe Deselected
    Indeterminate.next shouldBe Selected
    Deselected.next shouldBe Selected
  }
}
