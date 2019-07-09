package scommons.client.ui

import scommons.react._
import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class ImageLabelWrapperSpec extends TestSpec with ShallowRendererUtils {

  it should "render only image" in {
    //given
    val image = ButtonImagesCss.folder
    val wrapper = new FunctionComponent[Unit] {
      protected def render(props: Props): ReactElement = {
        <.>()(
          ImageLabelWrapper(image, None)
        )
      }
    }

    //when
    val result = shallowRender(<(wrapper()).empty)
    
    //then
    assertNativeComponent(result, <.>()(
      <.img(^.className := image, ^.src := "")()
    ))
  }
  
  it should "render image and text aligned" in {
    //given
    val image = ButtonImagesCss.folder
    val text = "some text"
    val wrapper = new FunctionComponent[Unit] {
      protected def render(props: Props): ReactElement = {
        <.>()(
          ImageLabelWrapper(image, Some(text))
        )
      }
    }

    //when
    val result = shallowRender(<(wrapper()).empty)
    
    //then
    assertNativeComponent(result, <.>()(
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
    val wrapper = new FunctionComponent[Unit] {
      protected def render(props: Props): ReactElement = {
        <.>()(
          ImageLabelWrapper(image, Some(text), alignText = false)
        )
      }
    }

    //when
    val result = shallowRender(<(wrapper()).empty)
    
    //then
    assertNativeComponent(result, <.>()(
      <.img(^.className := image, ^.src := "")(),
      <.span(^.style := Map(
        "paddingLeft" -> "3px"
      ))(text)
    ))
  }
}
