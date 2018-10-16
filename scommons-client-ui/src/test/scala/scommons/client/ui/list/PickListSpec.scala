package scommons.client.ui.list

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.test.TestSpec
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.ButtonImagesCss

class PickListSpec extends TestSpec {

  it should "call onSelect when onAdd" in {
    //given
    val onSelect = mockFunction[Set[String], Unit]
    val props = PickListProps(List(ListBoxData("1", "Test")), onSelect = onSelect)
    val renderer = createRenderer()
    renderer.render(<(PickList())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val lists = findProps(comp, ListBox)
    lists.size shouldBe 2
    val srcList = lists.head
    val ids = Set("1")
    
    //then
    onSelect.expects(ids)

    //when & then
    srcList.onSelect(ids)
    val compV2 = renderer.getRenderOutput()
    assertPickList(compV2, props, selectedSourceIds = ids, addEnabled = true)

    //when & then
    findComponentProps(compV2, PickButtons).onAdd()
    val compV3 = renderer.getRenderOutput()
    assertPickList(compV3, props.copy(selectedIds = ids), addAllEnabled = false, removeAllEnabled = true)
  }

  it should "call onSelect when onRemove" in {
    //given
    val onSelect = mockFunction[Set[String], Unit]
    val props = PickListProps(List(ListBoxData("1", "Test")), selectedIds = Set("1"), onSelect = onSelect)
    val renderer = createRenderer()
    renderer.render(<(PickList())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val lists = findProps(comp, ListBox)
    lists.size shouldBe 2
    val dstList = lists(1)
    val ids = Set("1")
    
    //then
    onSelect.expects(Set.empty[String])

    //when & then
    dstList.onSelect(ids)
    val compV2 = renderer.getRenderOutput()
    assertPickList(compV2, props, selectedDestIds = ids,
      removeEnabled = true, addAllEnabled = false, removeAllEnabled = true)

    //when & then
    findComponentProps(compV2, PickButtons).onRemove()
    val compV3 = renderer.getRenderOutput()
    assertPickList(compV3, props.copy(selectedIds = Set.empty[String]))
  }

  it should "call onSelect when onAddAll" in {
    //given
    val onSelect = mockFunction[Set[String], Unit]
    val props = PickListProps(List(ListBoxData("1", "Test")), onSelect = onSelect)
    val renderer = createRenderer()
    renderer.render(<(PickList())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val ids = Set("1")

    //then
    onSelect.expects(ids)

    //when
    findComponentProps(comp, PickButtons).onAddAll()
    
    //then
    val compV2 = renderer.getRenderOutput()
    assertPickList(compV2, props.copy(selectedIds = ids), addAllEnabled = false, removeAllEnabled = true)
  }

  it should "call onSelect when onRemoveAll" in {
    //given
    val onSelect = mockFunction[Set[String], Unit]
    val props = PickListProps(List(ListBoxData("1", "Test")), selectedIds = Set("1"), onSelect = onSelect)
    val renderer = createRenderer()
    renderer.render(<(PickList())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()

    //then
    onSelect.expects(Set.empty[String])

    //when
    findComponentProps(comp, PickButtons).onRemoveAll()
    
    //then
    val compV2 = renderer.getRenderOutput()
    assertPickList(compV2, props.copy(selectedIds = Set.empty[String]))
  }

  it should "reset selectedIds if selectedIds changed when componentWillReceiveProps" in {
    //given
    val prevProps = PickListProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ))
    val renderer = createRenderer()
    renderer.render(<(PickList())(^.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    assertPickList(comp, prevProps)

    val props = prevProps.copy(selectedIds = Set("1"))

    //when
    renderer.render(<(PickList())(^.wrapped := props)())

    //then
    val compV2 = renderer.getRenderOutput()
    assertPickList(compV2, props, removeAllEnabled = true)
  }

  it should "reset selectedIds if preSelectedIds changed when componentWillReceiveProps" in {
    //given
    val prevProps = PickListProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), selectedIds = Set("1"))
    val renderer = createRenderer()
    renderer.render(<(PickList())(^.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    assertPickList(comp, prevProps, removeAllEnabled = true)

    val props = prevProps.copy(preSelectedIds = Set("2"))

    //when
    renderer.render(<(PickList())(^.wrapped := props)())

    //then
    val compV2 = renderer.getRenderOutput()
    assertPickList(compV2, props, addAllEnabled = false, removeAllEnabled = true)
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
    val result = shallowRender(comp)

    //then
    assertPickList(result, props)
  }

  it should "render component with preSelected dest items" in {
    //given
    val props = PickListProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), preSelectedIds = Set("2"))
    val renderer = createRenderer()
    renderer.render(<(PickList())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val lists = findProps(comp, ListBox)
    lists.size shouldBe 2
    val dstList = lists(1)
    
    //when
    dstList.onSelect(Set("2"))

    //then
    assertPickList(renderer.getRenderOutput(), props,
      selectedDestIds = Set("2")
    )
  }

  it should "render component with selected source and dest items" in {
    //given
    val props = PickListProps(List(
      ListBoxData("1", "Test"),
      ListBoxData("2", "Test2")
    ), selectedIds = Set("2"))
    val renderer = createRenderer()
    renderer.render(<(PickList())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val lists = findProps(comp, ListBox)
    lists.size shouldBe 2
    val srcList = lists.head
    val dstList = lists(1)
    
    //when
    srcList.onSelect(Set("1"))
    dstList.onSelect(Set("2"))

    //then
    assertPickList(renderer.getRenderOutput(), props,
      selectedSourceIds = Set("1"),
      selectedDestIds = Set("2"),
      addEnabled = true,
      removeEnabled = true,
      removeAllEnabled = true
    )
  }

  private def assertPickList(result: ComponentInstance,
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

    assertDOMComponent(result, <.div(^.className := "row-fluid")(), { case List(src, btns, dst) =>
      assertDOMComponent(src, <.div(^.className := "span5")(), { case List(title, hr, list) =>
        assertDOMComponent(title, <.strong()(props.sourceTitle))
        assertDOMComponent(hr, <.hr(^.style := Map("margin" -> "7px 0"))())
        assertComponent(list, ListBox) { case ListBoxProps(items, srcSelectedIds, _) =>
          items shouldBe sourceItems
          srcSelectedIds shouldBe selectedSourceIds
        }
      })
      assertComponent(btns, PickButtons) {
        case PickButtonsProps(add, remove, addAll, removeAll, _, _, _, _, className) =>
          className shouldBe Some("span2")
          add shouldBe addEnabled
          remove shouldBe removeEnabled
          addAll shouldBe addAllEnabled
          removeAll shouldBe removeAllEnabled
      }
      assertDOMComponent(dst, <.div(^.className := "span5")(), { case List(title, hr, list) =>
        assertDOMComponent(title, <.strong()(props.destTitle))
        assertDOMComponent(hr, <.hr(^.style := Map("margin" -> "7px 0"))())
        assertComponent(list, ListBox) { case ListBoxProps(items, dstSelectedIds, _) =>
          items shouldBe destItems
          dstSelectedIds shouldBe selectedDestIds
        }
      })
    })
  }
}
