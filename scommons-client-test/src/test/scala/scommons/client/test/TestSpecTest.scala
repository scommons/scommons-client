package scommons.client.test

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.react.UiComponent

import scala.scalajs.js.JavaScriptException

class TestSpecTest extends TestSpec {

  private val emptyComp = React.createClass[TestSpecTestProps, Unit] { _ =>
    <.div.empty
  }

  private val comp1 = React.createClass[TestSpecTestProps, Unit] { _ =>
    <.div()(
      <(TestSpecTest())(^.wrapped := TestSpecTestProps(3))()
    )
  }

  private val comp2 = React.createClass[TestSpecTestProps, Unit] { _ =>
    <.div()(
      <(TestSpecTest())(^.wrapped := TestSpecTestProps(1))(),
      <(TestSpecTest())(^.wrapped := TestSpecTestProps(2))()
    )
  }

  it should "fail if comp not found when findComponentProps" in {
    //given
    val comp = shallowRender(<(emptyComp)()())
    val searchComp = TestSpecTest

    //when
    val e = the[IllegalStateException] thrownBy {
      findComponentProps(comp, searchComp)
    }

    //then
    e.getMessage shouldBe s"UiComponent $searchComp not found"
  }

  it should "return props when findComponentProps" in {
    //given
    val comp = shallowRender(<(comp2)()())

    //when
    val result = findComponentProps(comp, TestSpecTest)

    //then
    result shouldBe TestSpecTestProps(1)
  }

  it should "return components props when findProps" in {
    //given
    val comp = shallowRender(<(comp2)()())

    //when
    val result = findProps(comp, TestSpecTest)

    //then
    result shouldBe List(
      TestSpecTestProps(1),
      TestSpecTestProps(2)
    )
  }

  it should "fail if more than one comp when findRenderedComponentProps" in {
    //given
    val component = renderIntoDocument(<(comp2)()())

    //when
    val e = the[JavaScriptException] thrownBy {
      findRenderedComponentProps(component, TestSpecTest)
    }

    //then
    e.getMessage() should include ("Error: Did not find exactly one match (found: 2)")
  }

  it should "return props when findRenderedComponentProps" in {
    //given
    val component = renderIntoDocument(<(comp1)()())

    //when
    val result = findRenderedComponentProps(component, TestSpecTest)

    //then
    result shouldBe TestSpecTestProps(3)
  }
}

case class TestSpecTestProps(test: Int)

object TestSpecTest extends UiComponent[TestSpecTestProps] {

  protected def create(): ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped
    <.div()(s"test: ${props.test}")
  }
}
