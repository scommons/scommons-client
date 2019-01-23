package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class ImageLabelWrapperSpec extends TestSpec with ShallowRendererUtils {

  it should "render only image" in {
    //given
    val image = ButtonImagesCss.folder
    val wrapper = React.createClass[Unit, Unit] { _ =>
      <.div()(
        ImageLabelWrapper(image, None)
      )
    }
    val component = <(wrapper).empty

    //when
    val result = shallowRender(component)
    
    //then
    assertNativeComponent(result, <.div()(
      <.img(^.className := image, ^.src := "")()
    ))
  }
  
  it should "render image and text aligned" in {
    //given
    val image = ButtonImagesCss.folder
    val text = "some text"
    val wrapper = React.createClass[Unit, Unit] { _ =>
      <.div()(
        ImageLabelWrapper(image, Some(text))
      )
    }
    val component = <(wrapper).empty

    //when
    val result = shallowRender(component)
    
    //then
    assertNativeComponent(result, <.div()(
      <.img(^.className := image, ^.src := "")(),
      <.span(^.style := Map(
        "paddingLeft" -> "3px",
        "verticalAlign" -> "middle"
      ))(text)
    ))
  }
  
  it should "render image and text not aligned" in {
    //given
    val image = ButtonImagesCss.folder
    val text = "some text"
    val wrapper = React.createClass[Unit, Unit] { _ =>
      <.div()(
        ImageLabelWrapper(image, Some(text), alignText = false)
      )
    }
    val component = <(wrapper).empty

    //when
    val result = shallowRender(component)
    
    //then
    assertNativeComponent(result, <.div()(
      <.img(^.className := image, ^.src := "")(),
      <.span(^.style := Map(
        "paddingLeft" -> "3px"
      ))(text)
    ))
  }
}
