package scommons.client.ui.list

import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.ImageLabelWrapper
import scommons.client.ui.list.ListBoxCss._
import scommons.react._

case class ListBoxProps(items: List[ListBoxData],
                        selectedIds: Set[String] = Set.empty,
                        onSelect: Set[String] => Unit = _ => ())

object ListBox extends ClassComponent[ListBoxProps] {

  private type ListBoxSelf = Self[PropsType, ListBoxState]

  private case class ListBoxState(selectedIds: Set[String])

  protected def create(): ReactClass = createClass[ListBoxState](
    getInitialState = { self =>
      ListBoxState(self.props.wrapped.selectedIds)
    },
    componentDidUpdate = { (self, prevProps, _) =>
      val props = self.props.wrapped
      if (prevProps.wrapped.selectedIds != props.selectedIds) {
        self.setState(_.copy(selectedIds = props.selectedIds))
      }
    },
    render = { self =>
      val props = self.props.wrapped

      val items = props.items.map { data =>
        val selectedClass = if (isSelected(self.state, data)) listBoxSelectedItem else ""
        
        <.div(
          ^.key := data.id,
          ^.className := s"$listBoxItem $selectedClass",
          ^.onClick := itemClick(self, data)
        )(
          data.image match {
            case None => data.label
            case Some(image) => ImageLabelWrapper(image, Some(data.label))
          }
        )
      }

      <.>()(
        items
      )
    }
  )

  private def isSelected(state: ListBoxState, item: ListBoxData): Boolean = {
    state.selectedIds.contains(item.id)
  }

  private def itemClick(self: ListBoxSelf, data: ListBoxData): MouseSyntheticEvent => Unit = { event =>
    val isMultiSelect = event.ctrlKey || event.metaKey
    val selected = isSelected(self.state, data)
    
    val selectedIds =
      if (isMultiSelect) {
        if (selected)
          self.state.selectedIds - data.id
        else
          self.state.selectedIds + data.id
      }
      else Set(data.id)
    
    if (self.state.selectedIds != selectedIds) {
      self.setState(s => s.copy(selectedIds = selectedIds))

      self.props.wrapped.onSelect(selectedIds)
    }
  }
}
