package scommons.client.ui.list

import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.statictags.Element
import scommons.react._
import scommons.react.dom._

case class PickButtonsProps(addEnabled: Boolean = true,
                            removeEnabled: Boolean = true,
                            addAllEnabled: Boolean = true,
                            removeAllEnabled: Boolean = true,
                            onAdd: () => Unit = () => (),
                            onRemove: () => Unit = () => (),
                            onAddAll: () => Unit = () => (),
                            onRemoveAll: () => Unit = () => (),
                            className: Option[String] = None)

object PickButtons extends FunctionComponent[PickButtonsProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped
    
    def btn(style: Map[String, String],
            title: String,
            text: String,
            enabled: Boolean,
            onClick: () => Unit): Element = {
      
      <.button(
        ^.`type` := "button",
        ^.className := "btn",
        ^.style := style,
        ^.title := title,
        ^.disabled := !enabled,
        ^.onClick := { _: MouseSyntheticEvent =>
          onClick()
        }
      )(text)
    }

    <.div(props.className.map(cn => ^.className := cn))(
      <.div(^.className := "btn-group btn-group-vertical hidden-phone")(
        btn(btnVert, "Add", ">", props.addEnabled, props.onAdd),
        btn(btnVert, "Remove", "<", props.removeEnabled, props.onRemove),
        btn(btnVert, "Add All", ">>", props.addAllEnabled, props.onAddAll),
        btn(btnVert, "Remove All", "<<", props.removeAllEnabled, props.onRemoveAll)
      ),
      
      <.div(^.className := "btn-group visible-phone", ^.style := btnGroupHoriz)(
        btn(btnHoriz, "Add", ">", props.addEnabled, props.onAdd),
        btn(btnHoriz, "Remove", "<", props.removeEnabled, props.onRemove),
        btn(btnHoriz, "Add All", ">>", props.addAllEnabled, props.onAddAll),
        btn(btnHoriz, "Remove All", "<<", props.removeAllEnabled, props.onRemoveAll)
      )
    )
  }

  private val btnVert = Map("width" -> "35px")
  private val btnHoriz = Map("height" -> "30px", "writingMode" -> "tb-rl")
  private val btnGroupHoriz = Map("margin" -> "10px 0")
}
