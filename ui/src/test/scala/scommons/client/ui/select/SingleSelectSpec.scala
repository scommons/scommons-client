package scommons.client.ui.select

import scommons.client.ui.select.raw._
import scommons.react.test.TestSpec
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.raw.ShallowInstance
import scommons.react.test.util.ShallowRendererUtils

import scala.scalajs.js

class SingleSelectSpec extends TestSpec
  with ShallowRendererUtils
  with TestDOMUtils {

  it should "call onSelectChange(Some(data)) when onChange(option)" in {
    //given
    val onSelectChange = mockFunction[Option[SelectData], Unit]
    val data = SelectData("test", "Test")
    val props = SingleSelectProps(None, List(data), onSelectChange = onSelectChange)
    val component = shallowRender(<(SingleSelect())(^.wrapped := props)())

    //then
    onSelectChange.expects(Some(data))

    //when
    component.props.onChange(new ReactSelectOption() {
      override val value = data.value
      override val label = data.label
    })
  }

  it should "call onSelectChange(None) when onChange(null)" in {
    //given
    val onSelectChange = mockFunction[Option[SelectData], Unit]
    val data = SelectData("test", "Test")
    val props = SingleSelectProps(None, List(data), onSelectChange = onSelectChange)
    val component = shallowRender(<(SingleSelect())(^.wrapped := props)())

    //then
    onSelectChange.expects(None)

    //when
    component.props.onChange(null)
  }

  it should "call onSelectChange(None) when onChange(undefined)" in {
    //given
    val onSelectChange = mockFunction[Option[SelectData], Unit]
    val data = SelectData("test", "Test")
    val props = SingleSelectProps(None, List(data), onSelectChange = onSelectChange)
    val component = shallowRender(<(SingleSelect())(^.wrapped := props)())

    //then
    onSelectChange.expects(None)

    //when
    component.props.onChange(js.undefined)
  }

  it should "call onInputChange when onInputChange with 'input-change' action" in {
    //given
    val onInputChange = mockFunction[String, Unit]
    val data = SelectData("test", "Test")
    val props = SingleSelectProps(None, List(data), onInputChange = Some(onInputChange))
    val component = shallowRender(<(SingleSelect())(^.wrapped := props)())
    val inputValue = "some input"

    //then
    onInputChange.expects(inputValue)

    //when
    component.props.onInputChange(inputValue, new ReactSelectAction() {
      override val action = "input-change"
    })
  }

  it should "not call onInputChange when onInputChange with 'any-other' action" in {
    //given
    val onInputChange = mockFunction[String, Unit]
    val data = SelectData("test", "Test")
    val props = SingleSelectProps(None, List(data), onInputChange = Some(onInputChange))
    val component = shallowRender(<(SingleSelect())(^.wrapped := props)())
    val inputValue = "some input"

    //then
    onInputChange.expects(*).never()

    //when
    component.props.onInputChange(inputValue, new ReactSelectAction() {
      override val action = "any-other-action"
    })
  }

  it should "render component" in {
    //given
    val props = SingleSelectProps(None, List(
      SelectData("test", "Test"),
      SelectData("test2", "Test2")
    ))
    val component = <(SingleSelect())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertSingleSelect(result, props)
  }

  it should "render clearable component with selected option" in {
    //given
    val selected = SelectData("test", "Test")
    val props = SingleSelectProps(Some(selected), List(
      selected,
      SelectData("test2", "Test2")
    ), isClearable = true)
    val component = <(SingleSelect())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertSingleSelect(result, props)
  }

  it should "render searchable component with loading" in {
    //given
    val selected = SelectData("test", "Test")
    val props = SingleSelectProps(Some(selected), List(
      selected,
      SelectData("test2", "Test2")
    ), isSearchable = true, isLoading = true)
    val component = <(SingleSelect())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertSingleSelect(result, props)
  }

  it should "render readOnly component" in {
    //given
    val selected = SelectData("test", "Test")
    val props = SingleSelectProps(Some(selected), List(
      selected,
      SelectData("test2", "Test2")
    ), readOnly = true)
    val component = <(SingleSelect())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertSingleSelect(result, props)
  }

  it should "render component in the DOM" in {
    //given
    val props = SingleSelectProps(None, List(
      SelectData("test", "Test"),
      SelectData("test2", "Test2")
    ))
    val component = <(SingleSelect())(^.wrapped := props)()

    //when
    domRender(component)

    //then
    val container = domContainer.querySelector(".react-select-container")
    
    val control = container.firstElementChild
    control.getAttribute("class") should include ("react-select__control")
  }
  
  private def assertSingleSelect(result: ShallowInstance, props: SingleSelectProps): Unit = {
    result.`type` shouldBe NativeReactSelect
    result.props.className shouldBe "react-select-container"
    result.props.classNamePrefix shouldBe "react-select"
    result.props.menuPlacement shouldBe "auto"
    result.props.isClearable shouldBe props.isClearable
    result.props.isDisabled shouldBe props.readOnly
    result.props.isSearchable shouldBe props.isSearchable
    result.props.isLoading shouldBe props.isLoading

    result.props.asInstanceOf[js.Dynamic].selectDynamic("value").asInstanceOf[js.Array[ReactSelectOption]].map { o =>
      SelectData(o.value.getOrElse(""), o.label.getOrElse(""))
    }.toList shouldBe props.selected.toList

    result.props.options.asInstanceOf[js.Array[ReactSelectOption]].map { o =>
      SelectData(o.value.getOrElse(""), o.label.getOrElse(""))
    }.toList shouldBe props.options
  }
}
