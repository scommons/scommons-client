package scommons.client.ui.select

import scommons.client.ui.select.raw.NativeSelect._
import scommons.client.ui.select.raw.{ReactSelectAction, ReactSelectOption}
import scommons.react._

import scala.scalajs.js

case class SingleSelectProps(selected: Option[SelectData],
                             options: List[SelectData] = Nil,
                             onSelectChange: Option[SelectData] => Unit = _ => (),
                             isClearable: Boolean = false,
                             isSearchable: Boolean = false,
                             isLoading: Boolean = false,
                             onInputChange: Option[String => Unit] = None,
                             readOnly: Boolean = false)

object SingleSelect extends FunctionComponent[SingleSelectProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    <.ReactSelect(
      ^.className := "react-select-container",
      ^.classNamePrefix := "react-select",
      ^.menuPlacement := "auto", // bottom, top
      ^.selectedOptions := props.selected.toList.map(v =>
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
      ^.onSingleSelectChange := { value: ReactSelectOption =>
        val maybeValue =
          if (value == null || js.isUndefined(value)) None
          else Some(value)
        
        props.onSelectChange(maybeValue.map { v =>
          SelectData(v.value.getOrElse(""), v.label.getOrElse(""))
        })
      },
      ^.isClearable := props.isClearable,
      ^.isDisabled := props.readOnly,
      ^.isSearchable := props.isSearchable,
      ^.isLoading := props.isLoading,
      props.onInputChange.map { onInputChange =>
        ^.onInputChange := { (inputValue: String, a: ReactSelectAction) =>
          if (a.action == "input-change") {
            onInputChange(inputValue)
          }
        }
      }
    )()
  }
}
