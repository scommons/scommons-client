package scommons.client.ui.select

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.UiComponent
import scommons.client.ui.select.raw.NativeSelect._
import scommons.client.ui.select.raw.{ReactSelectAction, ReactSelectOption}

case class SelectProps(selectedOptions: List[SelectData] = Nil,
                       options: List[SelectData] = Nil,
                       onSelectChange: SelectData => Unit = _ => (),
                       isLoading: Boolean = false,
                       onInputChange: String => Unit = _ => (),
                       menuPlacement: String = "auto" // bottom, top
                      )

object Select extends UiComponent[SelectProps] {

  def apply(): ReactClass = reactClass
  lazy val reactClass: ReactClass = createComp

  private def createComp: ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped

    <.ReactSelect(
      ^.className := "react-select-container",
      ^.classNamePrefix := "react-select",
      ^.selectedOptions := props.selectedOptions.map(v =>
        new ReactSelectOption() {
          override val value = v.value
          override val label = v.label
        }
      ),
      ^.options := props.options.map { v =>
        new ReactSelectOption() {
          override val value = v.value
          override val label = v.label
        }
      },
      ^.onSelectChange := { v: ReactSelectOption =>
        props.onSelectChange(SelectData(v.value.getOrElse(""), v.label.getOrElse("")))
      },
      ^.isLoading := props.isLoading,
      ^.onInputChange := { (inputValue: String, a: ReactSelectAction) =>
        if (a.action == "input-change") {
          props.onInputChange(inputValue)
        }
      },
      ^.menuPlacement := props.menuPlacement
    )()
  }
}
