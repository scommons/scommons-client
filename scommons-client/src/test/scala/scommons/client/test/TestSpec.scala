package scommons.client.test

import org.scalamock.matchers.Matchers
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Inside}
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.UiComponent

trait TestSpec extends FlatSpec
  with Matchers
  with Inside
  with ShallowRendererUtils
  with MockFactory {

  def findComponentProps[T](renderedComp: ComponentInstance, searchComp: UiComponent[T]): T = {
    getComponentProps[T](findComponentWithType(renderedComp, searchComp.reactClass))
  }
}
