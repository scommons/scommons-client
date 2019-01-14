package scommons.client.ui.select

import org.scalatest.{Assertion, Succeeded}
import scommons.react.test.dom.AsyncTestSpec
import scommons.react.test.raw.ShallowRenderer.ComponentInstance
import scommons.react.test.util.ShallowRendererUtils

import scala.concurrent.{Future, Promise}

class SearchSelectSpec extends AsyncTestSpec with ShallowRendererUtils {

  it should "call onChange function when onSelectChange" in {
    //given
    val onChange = mockFunction[Option[SelectData], Unit]
    val props = SearchSelectProps(None, onChange = onChange)
    val component = shallowRender(<(SearchSelect())(^.wrapped := props)())
    val selectProps = findComponentProps(component, SingleSelect)
    val data = SelectData("test", "Test")

    //then
    onChange.expects(Some(data))

    //when
    selectProps.onSelectChange(Some(data))
    
    Succeeded
  }

  it should "set isLoading and call onLoad when onInputChange" in {
    //given
    val onLoad = mockFunction[String, Future[List[SelectData]]]
    val props = SearchSelectProps(None, onLoad = onLoad)
    val renderer = createRenderer()
    renderer.render(<(SearchSelect())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val selectProps = findComponentProps(comp, SingleSelect)
    selectProps.isLoading shouldBe false
    val inputValue = "some input"
    val promise = Promise[List[SelectData]]()

    onLoad.expects(inputValue).returning(promise.future)

    //when
    selectProps.onInputChange.get.apply(inputValue)
    
    //then
    eventually {
      val updatedComp = renderer.getRenderOutput()
      val updatedSelectProps = findComponentProps(updatedComp, SingleSelect)
      updatedSelectProps.isLoading shouldBe true
    }
  }

  it should "reset isLoading and set options when load succeeded" in {
    //given
    val onLoad = mockFunction[String, Future[List[SelectData]]]
    val props = SearchSelectProps(None, onLoad = onLoad)
    val renderer = createRenderer()
    renderer.render(<(SearchSelect())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val selectProps = findComponentProps(comp, SingleSelect)
    selectProps.isLoading shouldBe false
    val inputValue = "some input"
    val options = List(
      SelectData("test", "Test"),
      SelectData("test2", "Test2")
    )

    onLoad.expects(inputValue).returning(Future.successful(options))

    //when
    selectProps.onInputChange.get.apply(inputValue)
    
    //then
    eventually {
      val updatedComp = renderer.getRenderOutput()
      val updatedSelectProps = findComponentProps(updatedComp, SingleSelect)
      updatedSelectProps.isLoading shouldBe false
      updatedSelectProps.options shouldBe options
    }
  }

  it should "reset isLoading when load failed" in {
    //given
    val onLoad = mockFunction[String, Future[List[SelectData]]]
    val props = SearchSelectProps(None, onLoad = onLoad)
    val renderer = createRenderer()
    renderer.render(<(SearchSelect())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val selectProps = findComponentProps(comp, SingleSelect)
    selectProps.isLoading shouldBe false
    val inputValue = "some input"
    val promise = Promise[List[SelectData]]()

    onLoad.expects(inputValue).returning(promise.future)

    //when
    selectProps.onInputChange.get.apply(inputValue)

    //then
    var loaded = false
    eventually {
      val updatedComp = renderer.getRenderOutput()
      val updatedSelectProps = findComponentProps(updatedComp, SingleSelect)
      
      if (!loaded) {
        updatedSelectProps.isLoading shouldBe true

        if (updatedSelectProps.isLoading) {
          promise.failure(new Exception("load failed"))
          loaded = true
        }

        updatedSelectProps.isLoading shouldBe false //return failed assertion
      }
      else {
        updatedSelectProps.isLoading shouldBe false
      }
    }
  }

  it should "ignore stale load results" in {
    //given
    val onLoad = mockFunction[String, Future[List[SelectData]]]
    val props = SearchSelectProps(None, onLoad = onLoad)
    val renderer = createRenderer()
    renderer.render(<(SearchSelect())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val selectProps = findComponentProps(comp, SingleSelect)
    selectProps.isLoading shouldBe false
    val inputValue1 = "some input 1"
    val inputValue2 = "some input 2"
    val options = List(SelectData("2", "Results 2"))
    val promise = Promise[List[SelectData]]()

    onLoad.expects(inputValue1).returning(promise.future)
    onLoad.expects(inputValue2).returning(Future.successful(options))

    //when
    selectProps.onInputChange.get.apply(inputValue1)

    //then
    var loaded = false
    eventually {
      val updatedComp = renderer.getRenderOutput()
      val updatedSelectProps = findComponentProps(updatedComp, SingleSelect)
      
      if (!loaded) {
        updatedSelectProps.isLoading shouldBe true

        if (updatedSelectProps.isLoading) {
          updatedSelectProps.onInputChange.get.apply(inputValue2)
          promise.success(List(SelectData("1", "Stale Results")))
          loaded = true
        }

        updatedSelectProps.isLoading shouldBe false //return failed assertion
      }
      else {
        updatedSelectProps.isLoading shouldBe false
        updatedSelectProps.options shouldBe options
      }
    }
  }

  it should "ignore intermediate input values" in {
    //given
    val onLoad = mockFunction[String, Future[List[SelectData]]]
    val props = SearchSelectProps(None, onLoad = onLoad)
    val renderer = createRenderer()
    renderer.render(<(SearchSelect())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val selectProps = findComponentProps(comp, SingleSelect)
    selectProps.isLoading shouldBe false
    val inputValue1 = "some input 1"
    val inputValue2 = "some input 2"
    val options = List(SelectData("2", "Results 2"))

    onLoad.expects(inputValue1).never()
    onLoad.expects(inputValue2).returning(Future.successful(options))

    //when
    selectProps.onInputChange.get.apply(inputValue1)
    selectProps.onInputChange.get.apply(inputValue2)

    //then
    eventually {
      val updatedComp = renderer.getRenderOutput()
      val updatedSelectProps = findComponentProps(updatedComp, SingleSelect)
      updatedSelectProps.isLoading shouldBe false
      updatedSelectProps.options shouldBe options
    }
  }

  it should "render component" in {
    //given
    val props = SearchSelectProps(None)
    val component = <(SearchSelect())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertSearchSelect(result, props)
  }

  it should "render clearable component with selected data" in {
    //given
    val props = SearchSelectProps(Some(SelectData("test", "Test")), isClearable = true)
    val component = <(SearchSelect())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertSearchSelect(result, props)
  }

  it should "render readOnly component" in {
    //given
    val props = SearchSelectProps(Some(SelectData("test", "Test")), readOnly = true)
    val component = <(SearchSelect())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertSearchSelect(result, props)
  }

  private def assertSearchSelect(result: ComponentInstance, props: SearchSelectProps): Assertion = {
    assertComponent(result, SingleSelect) {
      case SingleSelectProps(
      selected,
      options,
      _,
      isClearable,
      isSearchable,
      isLoading,
      onInputChange,
      readOnly) =>
        selected shouldBe props.selected
        options shouldBe Nil
        isClearable shouldBe props.isClearable
        isSearchable shouldBe true
        isLoading shouldBe false
        readOnly shouldBe props.readOnly
        onInputChange should not be None
    }
  }
}
