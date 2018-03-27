package scommons.client.ui.page

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.statictags.Element
import scommons.client.ui.UiComponent

case class PaginationPanelProps(totalPages: Int,
                                selectedPage: Int = 1,
                                onPage: Int => Unit = _ => (),
                                alignment: PaginationAlignment = PaginationAlignment.Centered) {

  require(totalPages >= 1, s"totalPages($totalPages) should be greater than or equal 1")

  require(selectedPage >= 1 && selectedPage <= totalPages,
    s"selectedPage($selectedPage) should be between 1 and $totalPages")
}

object PaginationPanel extends UiComponent[PaginationPanelProps] {

  private case class PagingPanelState(selectedPage: Int)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, PagingPanelState](
    getInitialState = { self =>
      PagingPanelState(self.props.wrapped.selectedPage)
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped.selectedPage != props.selectedPage) {
        self.setState(_.copy(selectedPage = props.selectedPage))
      }
    },
    render = { self =>
      val props = self.props.wrapped

      def renderBtn(text: String, page: Int, attributes: Any*): Element = {
        <.li(attributes: _*)(
          <.a(
            ^.href := "",
            ^.onClick := { e: MouseSyntheticEvent =>
              e.preventDefault()
              props.onPage(page)
              self.setState(_.copy(selectedPage = page))
            }
          )(text)
        )
      }

      val buttons = <.ul()((1 to props.totalPages).flatMap { page =>
        def attributes(disabled: Boolean) =
          if (page == self.state.selectedPage) {
            Some(^.className := (if (disabled) "disabled" else "active"))
          }
          else None

        var elems = List(
          renderBtn(s"$page", page, attributes(false))
        )
        if (page == 1) {
          elems = renderBtn("<<", page, attributes(true)) +: elems
        }
        if (page == props.totalPages) {
          elems = elems :+ renderBtn(">>", page, attributes(true))
        }

        elems
      })

      <.div(^.className := props.alignment.style)(buttons)
    }
  )
}
