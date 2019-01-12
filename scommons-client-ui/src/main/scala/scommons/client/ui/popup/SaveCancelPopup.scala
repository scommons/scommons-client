package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import scommons.client.ui._
import scommons.client.util.ActionsData
import scommons.react.UiComponent

trait SaveCancelPopupProps {
  type DataType
  
  def show: Boolean
  def title: String
  def initialData: DataType
  def onSave: DataType => Unit
  def onCancel: () => Unit
  
  def isSaveEnabled(data: DataType): Boolean
  def render(data: DataType, requestFocus: Boolean, onChange: DataType => Unit, onSave: () => Unit): ReactElement
}

trait SaveCancelPopup[T <: SaveCancelPopupProps] extends UiComponent[T] {

  protected def create(): ReactClass = React.createClass[PropsType, Unit] { self =>
    <(SaveCancelPopup())(^.wrapped := self.props.wrapped)()
  }
}

object SaveCancelPopup extends UiComponent[SaveCancelPopupProps] {

  private case class SaveCancelPopupState(data: Any, opened: Boolean = false)

  protected def create(): ReactClass = React.createClass[PropsType, SaveCancelPopupState](
    getInitialState = { self =>
      SaveCancelPopupState(self.props.wrapped.initialData)
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped != props) {
        self.setState(_.copy(data = props.initialData, opened = false))
      }
    },
    render = { self =>
      val props = self.props.wrapped

      val onChange = { data: Any =>
        self.setState(_.copy(data = data))
      }
      val onSave = { () =>
        val currData = self.state.data.asInstanceOf[props.DataType]
        if (props.isSaveEnabled(currData)) {
          props.onSave(currData)
        }
      }

      val data = self.state.data.asInstanceOf[props.DataType]
      val actionCommands = if (props.isSaveEnabled(data)) enabledActions else disabledActions

      <(Modal())(^.wrapped := ModalProps(
        show = props.show,
        header = Some(props.title),
        buttons = List(Buttons.SAVE.copy(
          image = ButtonImagesCss.dbSave,
          disabledImage = ButtonImagesCss.dbSaveDisabled,
          primary = true
        ), Buttons.CANCEL),
        actions = ActionsData(actionCommands, _ => {
          case Buttons.SAVE.command => onSave()
          case _ => props.onCancel()
        }),
        onClose = props.onCancel,
        onOpen = { () =>
          self.setState(_.copy(opened = true))
        }
      ))(
        props.render(data, self.state.opened, onChange, onSave)
      )
    }
  )

  private val enabledActions = Set(Buttons.SAVE.command, Buttons.CANCEL.command)
  private val disabledActions = Set(Buttons.CANCEL.command)
}
