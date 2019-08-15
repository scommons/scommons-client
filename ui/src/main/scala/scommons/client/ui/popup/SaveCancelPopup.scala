package scommons.client.ui.popup

import scommons.client.ui._
import scommons.client.util.ActionsData
import scommons.react._
import scommons.react.hooks._

trait SaveCancelPopupProps {
  
  type DataType
  
  def title: String
  def initialData: DataType
  def onSave: DataType => Unit
  def onCancel: () => Unit
  
  def isSaveEnabled(data: DataType): Boolean
  def render(data: DataType, requestFocus: Boolean, onChange: DataType => Unit, onSave: () => Unit): ReactElement
}

trait SaveCancelPopup[T <: SaveCancelPopupProps] extends FunctionComponent[T] {

  protected def render(compProps: Props): ReactElement = {
    <(SaveCancelPopup())(^.wrapped := compProps.wrapped)()
  }
}

private object SaveCancelPopup extends FunctionComponent[SaveCancelPopupProps] {

  private case class SaveCancelPopupState(data: Any, opened: Boolean = false)

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped
    val (state, setState) = useStateUpdater(() => SaveCancelPopupState(props.initialData))

    val onChange = { data: Any =>
      setState(_.copy(data = data))
    }
    val onSave = { () =>
      val currData = state.data.asInstanceOf[props.DataType]
      if (props.isSaveEnabled(currData)) {
        props.onSave(currData)
      }
    }

    val data = state.data.asInstanceOf[props.DataType]
    val actionCommands = if (props.isSaveEnabled(data)) enabledActions else disabledActions

    <(Modal())(^.wrapped := ModalProps(
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
        setState(_.copy(opened = true))
      }
    ))(
      props.render(data, state.opened, onChange, onSave)
    )
  }

  private val enabledActions = Set(Buttons.SAVE.command, Buttons.CANCEL.command)
  private val disabledActions = Set(Buttons.CANCEL.command)
}
