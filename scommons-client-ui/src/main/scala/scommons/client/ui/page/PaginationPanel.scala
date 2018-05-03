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

  require(totalPages >= 1,
    s"totalPages($totalPages) should be greater than or equal 1")

  require(selectedPage >= 1 && selectedPage <= totalPages,
    s"selectedPage($selectedPage) should be between 1 and $totalPages")
}

object PaginationPanel extends UiComponent[PaginationPanelProps] {

  private case class PaginationPanelState(selectedPage: Int, selectedRange: Range)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, PaginationPanelState](
    getInitialState = { self =>
      val props = self.props.wrapped
      PaginationPanelState(props.selectedPage, getSelectedRange(props.selectedPage, props.totalPages))
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped.selectedPage != props.selectedPage) {
        self.setState(_.copy(
          selectedPage = props.selectedPage,
          selectedRange = getSelectedRange(props.selectedPage, props.totalPages)
        ))
      }
    },
    render = { self =>
      val props = self.props.wrapped
      val maxPage = props.totalPages

      def renderBtn(text: String, onClick: () => Unit, attributes: Any*): Element = {
        <.li(attributes: _*)(
          <.a(
            ^.href := "",
            ^.onClick := { e: MouseSyntheticEvent =>
              e.preventDefault()
              onClick()
            }
          )(text)
        )
      }

      def isPageSelected(page: Int): Boolean = page == self.state.selectedPage

      def pageBtn(text: String, page: Int, disableable: Boolean): Element = {
        val attributes =
          if (isPageSelected(page)) {
            Some(^.className := (if (disableable) "disabled" else "active"))
          }
          else None

        renderBtn(text, onClick = { () =>
          if (!isPageSelected(page)) {
            selectPage(page)
          }
        }, attributes)
      }

      def selectPage(pageToSelect: Int): Unit = {
        val page =
          if (pageToSelect < minPage) minPage
          else if (pageToSelect > maxPage) maxPage
          else pageToSelect

        props.onPage(page)
        self.setState(_.copy(
          selectedPage = page,
          selectedRange = getSelectedRange(page, maxPage)
        ))
      }

      def scrollBtn(text: String, scrollPages: Int): Element = {
        renderBtn(text, onClick = { () =>
          selectPage(self.state.selectedPage + scrollPages)
        }, None)
      }

      val selectedRange = self.state.selectedRange
      val buttons = selectedRange.map { page =>
        pageBtn(s"$page", page, disableable = false)
      }

      <.div(^.className := props.alignment.style)(
        <.ul()(
          pageBtn("<<", minPage, disableable = true),
          if (minPage < selectedRange.start) {
            Some(scrollBtn("<", -viewPages))
          }
          else None,
          buttons,
          if (maxPage > selectedRange.end) {
            Some(scrollBtn(">", viewPages))
          }
          else None,
          pageBtn(">>", maxPage, disableable = true)
        )
      )
    }
  )

  private val minPage = 1
  private val viewPages = 5

  private[page] def getSelectedRange(selectedPage: Int, maxPage: Int): Range = {
    if (maxPage <= viewPages) minPage to maxPage
    else {
      val start = selectedPage - (viewPages / 2)
      if (start < minPage) minPage to viewPages
      else {
        val end = start + viewPages - 1
        if (end > maxPage) (maxPage - viewPages + 1) to maxPage
        else start to end
      }
    }
  }
}
