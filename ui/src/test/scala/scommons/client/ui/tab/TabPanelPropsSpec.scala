package scommons.client.ui.tab

import org.scalatest.{FlatSpec, Matchers}

class TabPanelPropsSpec extends FlatSpec with Matchers {

  it should "fail if selectedIndex is out of items range" in {
    def assertFailedProps(props: => TabPanelProps, selectedIndex: Int): Unit = {
      val e = the[IllegalArgumentException] thrownBy {
        props
      }

      e.getMessage should include (
        s"selectedIndex($selectedIndex) should be within items indices"
      )
    }

    //when & then
    assertFailedProps(TabPanelProps(List.empty), 0)
    assertFailedProps(TabPanelProps(List.empty, -1), -1)
    assertFailedProps(TabPanelProps(List.empty, 1), 1)
    assertFailedProps(TabPanelProps(List(TabItemData("tab1")), 1), 1)
    assertFailedProps(TabPanelProps(List(TabItemData("tab1"), TabItemData("tab2")), 2), 2)
  }

  it should "return props if selectedIndex is within items range" in {
    //when & then
    TabPanelProps(List(TabItemData("tab1"))).selectedIndex shouldBe 0
    TabPanelProps(List(TabItemData("tab1"), TabItemData("tab2")), 1).selectedIndex shouldBe 1
  }
}
