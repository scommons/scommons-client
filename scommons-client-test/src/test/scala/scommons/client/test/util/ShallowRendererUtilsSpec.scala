package scommons.client.test.util

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.test.TestSpec

class ShallowRendererUtilsSpec extends TestSpec {

  case class Comp1Props(a: Int)
  case class Comp2Props(b: Boolean)

  private val comp1Class = React.createClass[Comp1Props, Unit] { _ =>
    <.p()("comp1Class")
  }
  private val comp2Class = React.createClass[Comp2Props, Unit] { _ =>
    <.div()(
      <(comp1Class)(^.wrapped := Comp1Props(1))(),
      <(comp1Class)(^.wrapped := Comp1Props(2))()
    )
  }

  it should "not find component when findComponents" in {
    //given
    val comp = shallowRender(<(comp1Class)(^.wrapped := Comp1Props(123))())

    //when & then
    findComponents(comp, comp1Class) shouldBe Nil
    findComponents(comp, comp2Class) shouldBe Nil
  }

  it should "find all components when findComponents" in {
    //given
    val comp = shallowRender(<(comp2Class)(^.wrapped := Comp2Props(true))())

    //when
    val results = findComponents(comp, comp1Class)

    //then
    results.map(getComponentProps[Comp1Props]) shouldBe List(Comp1Props(1), Comp1Props(2))
  }
}
