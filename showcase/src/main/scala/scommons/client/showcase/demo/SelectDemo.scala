package scommons.client.showcase.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.scalajs.dom
import scommons.client.ui.select._
import scommons.client.ui.{ImageCheckBox, ImageCheckBoxProps, TriState}

import scala.concurrent.Promise
import scala.concurrent.duration._

object SelectDemo {
  
  private case class SelectDemoState(singleSelectValue: Option[SelectData] = None,
                                     singleSelectClearable: Boolean = false,
                                     singleSelectSearchable: Boolean = false,
                                     singleSelectReadOnly: Boolean = false,
                                     searchSelectValue: Option[SelectData] = None,
                                     searchSelectClearable: Boolean = false,
                                     searchSelectReadOnly: Boolean = false)

  def apply(): ReactClass = reactClass
  private lazy val reactClass = createComp
  
  private def createComp = React.createClass[Unit, SelectDemoState](
    getInitialState = { _ =>
      SelectDemoState()
    },
    render = { self =>
      <.div()(
        <.h2()("SingleSelect"),
        <.p()("Demonstrates single select functionality."),
        <.p()(
          <(SingleSelect())(^.wrapped := SingleSelectProps(
            selected = self.state.singleSelectValue,
            options = List(
              SelectData("chocolate", "Chocolate"),
              SelectData("strawberry", "Strawberry"),
              SelectData("vanilla", "Vanilla")
            ),
            isClearable = self.state.singleSelectClearable,
            isSearchable = self.state.singleSelectSearchable,
            readOnly = self.state.singleSelectReadOnly,
            onSelectChange = { v =>
              self.setState(s => s.copy(singleSelectValue = v))
            }
          ))(),
          <.form(^.className := "form-inline")(
            <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
              value = TriState(self.state.singleSelectReadOnly),
              image = "",
              text = "Read-only",
              onChange = { v =>
                self.setState(s => s.copy(singleSelectReadOnly = TriState.isSelected(v)))
              }
            ))(),
            <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
              value = TriState(self.state.singleSelectClearable),
              image = "",
              text = "Clearable",
              onChange = { v =>
                self.setState(s => s.copy(singleSelectClearable = TriState.isSelected(v)))
              }
            ))(),
            <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
              value = TriState(self.state.singleSelectSearchable),
              image = "",
              text = "Searchable",
              onChange = { v =>
                self.setState(s => s.copy(singleSelectSearchable = TriState.isSelected(v)))
              }
            ))()
          )
        ),
        <.h2()("SearchSelect"),
        <.p()("Demonstrates select with search functionality."),
        <.p()(
          <(SearchSelect())(^.wrapped := SearchSelectProps(
            selected = self.state.searchSelectValue,
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
            isClearable = self.state.searchSelectClearable,
            readOnly = self.state.searchSelectReadOnly,
            onChange = { v =>
              self.setState(s => s.copy(searchSelectValue = v))
            }
          ))(),
          <.form(^.className := "form-inline")(
            <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
              value = TriState(self.state.searchSelectReadOnly),
              image = "",
              text = "Read-only",
              onChange = { v =>
                self.setState(s => s.copy(searchSelectReadOnly = TriState.isSelected(v)))
              }
            ))(),
            <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
              value = TriState(self.state.searchSelectClearable),
              image = "",
              text = "Clearable",
              onChange = { v =>
                self.setState(s => s.copy(searchSelectClearable = TriState.isSelected(v)))
              }
            ))()
          )
        )
      )
    }
  )
}
