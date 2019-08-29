package scommons.client.showcase.demo

import org.scalajs.dom
import scommons.client.ui.select._
import scommons.client.ui.{ImageCheckBox, ImageCheckBoxProps, TriState}
import scommons.react._
import scommons.react.hooks._

import scala.concurrent.Promise
import scala.concurrent.duration._

object SelectDemo extends FunctionComponent[Unit] {
  
  private case class SelectDemoState(singleSelectValue: Option[SelectData] = None,
                                     singleSelectClearable: Boolean = false,
                                     singleSelectSearchable: Boolean = false,
                                     singleSelectReadOnly: Boolean = false,
                                     searchSelectValue: Option[SelectData] = None,
                                     searchSelectClearable: Boolean = false,
                                     searchSelectReadOnly: Boolean = false)

  protected def render(props: Props): ReactElement = {
    val (state, setState) = useStateUpdater(() => SelectDemoState())
    
    <.div()(
      <.h2()("SingleSelect"),
      <.p()("Demonstrates single select functionality."),
      <.p()(
        <(SingleSelect())(^.wrapped := SingleSelectProps(
          selected = state.singleSelectValue,
          options = List(
            SelectData("chocolate", "Chocolate"),
            SelectData("strawberry", "Strawberry"),
            SelectData("vanilla", "Vanilla")
          ),
          isClearable = state.singleSelectClearable,
          isSearchable = state.singleSelectSearchable,
          readOnly = state.singleSelectReadOnly,
          onSelectChange = { v =>
            setState(s => s.copy(singleSelectValue = v))
          }
        ))(),
        <.form(^.className := "form-inline")(
          <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
            value = TriState(state.singleSelectReadOnly),
            image = "",
            text = "Read-only",
            onChange = { v =>
              setState(s => s.copy(singleSelectReadOnly = TriState.isSelected(v)))
            }
          ))(),
          <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
            value = TriState(state.singleSelectClearable),
            image = "",
            text = "Clearable",
            onChange = { v =>
              setState(s => s.copy(singleSelectClearable = TriState.isSelected(v)))
            }
          ))(),
          <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
            value = TriState(state.singleSelectSearchable),
            image = "",
            text = "Searchable",
            onChange = { v =>
              setState(s => s.copy(singleSelectSearchable = TriState.isSelected(v)))
            }
          ))()
        )
      ),
      <.h2()("SearchSelect"),
      <.p()("Demonstrates select with search functionality."),
      <.p()(
        <(SearchSelect())(^.wrapped := SearchSelectProps(
          selected = state.searchSelectValue,
          onLoad = { _ =>
            val promise = Promise[List[SelectData]]()

            var handleId = 0
            handleId = dom.window.setTimeout({ () =>
              dom.window.clearTimeout(handleId)

              promise.success(List(
                SelectData("chocolate", "Chocolate"),
                SelectData("strawberry", "Strawberry"),
                SelectData("vanilla", "Vanilla")
              ))
            }, 1.second.toMillis.toDouble)

            promise.future
          },
          isClearable = state.searchSelectClearable,
          readOnly = state.searchSelectReadOnly,
          onChange = { v =>
            setState(s => s.copy(searchSelectValue = v))
          }
        ))(),
        <.form(^.className := "form-inline")(
          <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
            value = TriState(state.searchSelectReadOnly),
            image = "",
            text = "Read-only",
            onChange = { v =>
              setState(s => s.copy(searchSelectReadOnly = TriState.isSelected(v)))
            }
          ))(),
          <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
            value = TriState(state.searchSelectClearable),
            image = "",
            text = "Clearable",
            onChange = { v =>
              setState(s => s.copy(searchSelectClearable = TriState.isSelected(v)))
            }
          ))()
        )
      )
    )
  }
}
