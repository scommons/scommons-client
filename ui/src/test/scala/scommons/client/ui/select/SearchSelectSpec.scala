package scommons.client.ui.select

import org.scalatest.{Assertion, Succeeded}
import scommons.client.ui.select.SearchSelect._
import scommons.nodejs.test.AsyncTestSpec
import scommons.react.test._

import scala.concurrent.{Future, Promise}

class SearchSelectSpec extends AsyncTestSpec with BaseTestSpec with TestRendererUtils {

  SearchSelect.singleSelectComp = mockUiComponent("SingleSelect")

  it should "call onChange function when onSelectChange" in {
    //given
    val onChange = mockFunction[Option[SelectData], Unit]
    val props = SearchSelectProps(None, onChange = onChange)
    val component = testRender(<(SearchSelect())(^.wrapped := props)())
    val selectProps = findComponentProps(component, singleSelectComp)
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
    val renderer = createTestRenderer(<(SearchSelect())(^.wrapped := props)())
    val comp = renderer.root
    val selectProps = findComponentProps(comp, singleSelectComp)
    selectProps.isLoading shouldBe false
    val inputValue = "some input"
    val promise = Promise[List[SelectData]]()

    onLoad.expects(inputValue).returning(promise.future)

    //when
    selectProps.onInputChange.get.apply(inputValue)
    
    //then
    eventually {
      val updatedComp = renderer.root
      val updatedSelectProps = findComponentProps(updatedComp, singleSelectComp)
      updatedSelectProps.isLoading shouldBe true
    }
  }

  it should "reset isLoading and set options when load succeeded" in {
    //given
    val onLoad = mockFunction[String, Future[List[SelectData]]]
    val props = SearchSelectProps(None, onLoad = onLoad)
    val renderer = createTestRenderer(<(SearchSelect())(^.wrapped := props)())
    val comp = renderer.root
    val selectProps = findComponentProps(comp, singleSelectComp)
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
      val updatedComp = renderer.root
      val updatedSelectProps = findComponentProps(updatedComp, singleSelectComp)
      updatedSelectProps.isLoading shouldBe false
      updatedSelectProps.options shouldBe options
    }
  }

  it should "reset isLoading when load failed" in {
    //given
    val onLoad = mockFunction[String, Future[List[SelectData]]]
    val props = SearchSelectProps(None, onLoad = onLoad)
    val renderer = createTestRenderer(<(SearchSelect())(^.wrapped := props)())
    val comp = renderer.root
    val selectProps = findComponentProps(comp, singleSelectComp)
    selectProps.isLoading shouldBe false
    val inputValue = "some input"
    val promise = Promise[List[SelectData]]()

    onLoad.expects(inputValue).returning(promise.future)

    //when
    selectProps.onInputChange.get.apply(inputValue)

    //then
    var loaded = false
    eventually {
      val updatedComp = renderer.root
      val updatedSelectProps = findComponentProps(updatedComp, singleSelectComp)
      
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
    val renderer = createTestRenderer(<(SearchSelect())(^.wrapped := props)())
    val comp = renderer.root
    val selectProps = findComponentProps(comp, singleSelectComp)
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
      val updatedComp = renderer.root
      val updatedSelectProps = findComponentProps(updatedComp, singleSelectComp)
      
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
    val renderer = createTestRenderer(<(SearchSelect())(^.wrapped := props)())
    val comp = renderer.root
    val selectProps = findComponentProps(comp, singleSelectComp)
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
      val updatedComp = renderer.root
      val updatedSelectProps = findComponentProps(updatedComp, singleSelectComp)
      updatedSelectProps.isLoading shouldBe false
      updatedSelectProps.options shouldBe options
    }
  }

  it should "render component" in {
    //given
    val props = SearchSelectProps(None)
    val component = <(SearchSelect())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertSearchSelect(result, props)
  }

  it should "render clearable component with selected data" in {
    //given
    val props = SearchSelectProps(Some(SelectData("test", "Test")), isClearable = true)
    val component = <(SearchSelect())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertSearchSelect(result, props)
  }

  it should "render readOnly component" in {
    //given
    val props = SearchSelectProps(Some(SelectData("test", "Test")), readOnly = true)
    val component = <(SearchSelect())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertSearchSelect(result, props)
  }

  private def assertSearchSelect(result: TestInstance, props: SearchSelectProps): Assertion = {
    assertTestComponent(result, singleSelectComp) {
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
