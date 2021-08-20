package scommons.client.ui.list

import scommons.client.ui.ButtonImagesCss
import scommons.react.test._

class PickListSpec extends TestSpec with TestRendererUtils {

  it should "call onSelectChange when onAdd" in {
    //given
    val onSelectChange = mockFunction[Set[String], Boolean, Unit]
    val props = PickListProps(List(ListBoxData("1", "Test")), onSelectChange = onSelectChange)
    val renderer = createTestRenderer(<(PickList())(^.wrapped := props)())
    val comp = renderer.root
    val lists = findProps(comp, ListBox)
    lists.size shouldBe 2
    val srcList = lists.head
    val ids = Set("1")
    
    //then
    onSelectChange.expects(ids, true)

    //when & then
    srcList.onSelect(ids)
    val compV2 = renderer.root
    assertPickList(compV2, props, selectedSourceIds = ids, addEnabled = true)

    //when & then
    findComponentProps(compV2, PickButtons).onAdd()
    val compV3 = renderer.root
    assertPickList(compV3, props.copy(selectedIds = ids), addAllEnabled = false, removeAllEnabled = true)
  }

  it should "call onSelectChange when onRemove" in {
    //given
    val onSelectChange = mockFunction[Set[String], Boolean, Unit]
    val props = PickListProps(List(ListBoxData("1", "Test")), selectedIds = Set("1"), onSelectChange = onSelectChange)
    val renderer = createTestRenderer(<(PickList())(^.wrapped := props)())
    val comp = renderer.root
    val lists = findProps(comp, ListBox)
    lists.size shouldBe 2
    val dstList = lists(1)
    val ids = Set("1")
    
    //then
    onSelectChange.expects(ids, false)

    //when & then
    dstList.onSelect(ids)
    val compV2 = renderer.root
    assertPickList(compV2, props, selectedDestIds = ids,
      removeEnabled = true, addAllEnabled = false, removeAllEnabled = true)

    //when & then
    findComponentProps(compV2, PickButtons).onRemove()
    val compV3 = renderer.root
    assertPickList(compV3, props.copy(selectedIds = Set.empty[String]))
  }

  it should "call onSelectChange when onAddAll" in {
    //given
    val onSelectChange = mockFunction[Set[String], Boolean, Unit]
    val props = PickListProps(List(ListBoxData("1", "Test")), onSelectChange = onSelectChange)
    val renderer = createTestRenderer(<(PickList())(^.wrapped := props)())
    val comp = renderer.root
    val ids = Set("1")

    //then
    onSelectChange.expects(ids, true)

    //when
    findComponentProps(comp, PickButtons).onAddAll()
    
    //then
    val compV2 = renderer.root
    assertPickList(compV2, props.copy(selectedIds = ids), addAllEnabled = false, removeAllEnabled = true)
  }

  it should "call onSelectChange when onRemoveAll" in {
    //given
    val onSelectChange = mockFunction[Set[String], Boolean, Unit]
    val props = PickListProps(List(ListBoxData("1", "Test")), selectedIds = Set("1"), onSelectChange = onSelectChange)
    val renderer = createTestRenderer(<(PickList())(^.wrapped := props)())
    val comp = renderer.root
    val ids = Set("1")

    //then
    onSelectChange.expects(ids, false)

    //when
    findComponentProps(comp, PickButtons).onRemoveAll()
    
    //then
    val compV2 = renderer.root
    assertPickList(compV2, props.copy(selectedIds = Set.empty[String]))
  }

  it should "reset selectedIds if selectedIds changed when update" in {
    //given
    val props = PickListProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ))
    val renderer = createTestRenderer(<(PickList())(^.wrapped := props)())
    findProps(renderer.root, ListBox)(1).items shouldBe Nil

    //when & then
    renderer.update(<(PickList())(^.wrapped := props.copy(selectedIds = Set("2")))())
    findProps(renderer.root, ListBox)(1).items shouldBe List(
      ListBoxData("2", "Test2")
    )
    
    //when & then
    renderer.update(<(PickList())(^.wrapped := props.copy(selectedIds = Set("1")))())
    findProps(renderer.root, ListBox)(1).items shouldBe List(
      ListBoxData("1", "Test")
    )
  }

  it should "reset selectedIds if preSelectedIds changed when update" in {
    //given
    val props = PickListProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), selectedIds = Set("1"))
    val renderer = createTestRenderer(<(PickList())(^.wrapped := props)())
    findProps(renderer.root, ListBox)(1).items shouldBe List(
      ListBoxData("1", "Test")
    )

    //when & then
    renderer.update(<(PickList())(^.wrapped := props.copy(preSelectedIds = Set("2")))())
    findProps(renderer.root, ListBox)(1).items shouldBe List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    )
    
    //when & then
    renderer.update(<(PickList())(^.wrapped := props.copy(preSelectedIds = Set("1")))())
    findProps(renderer.root, ListBox)(1).items shouldBe List(
      ListBoxData("1", "Test")
    )
  }

  it should "render component" in {
    //given
    val props = PickListProps(List(
      ListBoxData("1", "Test", Some(ButtonImagesCss.acceptDisabled)),
      ListBoxData("2", "Test2"),
      ListBoxData("3", "Test3", Some(ButtonImagesCss.accept))
    ))
    val comp = <(PickList())(^.wrapped := props)()

    //when
    val result = createTestRenderer(comp).root

    //then
    assertPickList(result, props)
  }

  it should "render component with preSelected dest items" in {
    //given
    val props = PickListProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), preSelectedIds = Set("2"))
    val renderer = createTestRenderer(<(PickList())(^.wrapped := props)())
    val comp = renderer.root
    val lists = findProps(comp, ListBox)
    lists.size shouldBe 2
    val dstList = lists(1)
    
    //when
    dstList.onSelect(Set("2"))

    //then
    assertPickList(renderer.root, props,
      selectedDestIds = Set("2")
    )
  }

  it should "render component with selected source and dest items" in {
    //given
    val props = PickListProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), selectedIds = Set("2"))
    val renderer = createTestRenderer(<(PickList())(^.wrapped := props)())
    val comp = renderer.root
    val lists = findProps(comp, ListBox)
    lists.size shouldBe 2
    val srcList = lists.head
    val dstList = lists(1)
    
    //when
    srcList.onSelect(Set("1"))
    dstList.onSelect(Set("2"))

    //then
    assertPickList(renderer.root, props,
      selectedSourceIds = Set("1"),
      selectedDestIds = Set("2"),
      addEnabled = true,
      removeEnabled = true,
      removeAllEnabled = true
    )
  }

  private def assertPickList(result: TestInstance,
                             props: PickListProps,
                             selectedSourceIds: Set[String] = Set.empty,
                             selectedDestIds: Set[String] = Set.empty,
                             addEnabled: Boolean = false,
                             removeEnabled: Boolean = false,
                             addAllEnabled: Boolean = true,
                             removeAllEnabled: Boolean = false): Unit = {
    
    val selectedIds = props.selectedIds ++ props.preSelectedIds
    val sourceItems = props.items.filterNot(i => selectedIds.contains(i.id))
    val destItems = props.items.filter(i => selectedIds.contains(i.id))

    assertNativeComponent(result.children(0), <.div(^.className := "row-fluid")(), inside(_) {
      case List(src, btns, dst) =>
        assertNativeComponent(src, <.div(^.className := "span5")(), inside(_) {
          case List(title, hr, list) =>
            assertNativeComponent(title, <.strong()(props.sourceTitle))
            assertNativeComponent(hr, <.hr(^.style := Map("margin" -> "7px 0"))())
            assertTestComponent(list, ListBox) { case ListBoxProps(items, srcSelectedIds, _) =>
              items shouldBe sourceItems
              srcSelectedIds shouldBe selectedSourceIds
            }
        })
        assertTestComponent(btns, PickButtons) {
          case PickButtonsProps(add, remove, addAll, removeAll, _, _, _, _, className) =>
            className shouldBe Some("span2")
            add shouldBe addEnabled
            remove shouldBe removeEnabled
            addAll shouldBe addAllEnabled
            removeAll shouldBe removeAllEnabled
        }
        assertNativeComponent(dst, <.div(^.className := "span5")(), inside(_) {
          case List(title, hr, list) =>
            assertNativeComponent(title, <.strong()(props.destTitle))
            assertNativeComponent(hr, <.hr(^.style := Map("margin" -> "7px 0"))())
            assertTestComponent(list, ListBox) { case ListBoxProps(items, dstSelectedIds, _) =>
              items shouldBe destItems
              dstSelectedIds shouldBe selectedDestIds
            }
        })
    })
  }
}
