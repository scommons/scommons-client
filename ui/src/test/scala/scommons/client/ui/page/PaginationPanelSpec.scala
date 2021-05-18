package scommons.client.ui.page

import io.github.shogowada.statictags.Element
import scommons.client.ui.page.PaginationPanel._
import scommons.client.ui.page.PaginationPanelSpec._
import scommons.react.test._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

class PaginationPanelSpec extends TestSpec with TestRendererUtils {

  it should "call onPage once and select page button when click on it" in {
    //given
    val onPage = mockFunction[Int, Unit]
    val props = PaginationPanelProps(5, onPage = onPage)
    val comp = testRender(<(PaginationPanel())(^.wrapped := props)())
    val buttons = findComponents(comp, <.a.name)
    buttons.size shouldBe (props.totalPages + 2)
    val pages = findComponents(comp, <.li.name)
    pages.size shouldBe (props.totalPages + 2)
    pages(props.selectedPage).props.className shouldBe "active"
    val nextSelectPage = props.selectedPage + 1
    val event = mock[MouseSyntheticEventMock]

    //then
    onPage.expects(nextSelectPage).once()
    (event.preventDefault _).expects().twice()

    //when & then
    buttons(nextSelectPage).props.onClick(event.asInstanceOf[js.Any])
    pages(props.selectedPage).props.className shouldBe js.undefined
    pages(nextSelectPage).props.className shouldBe "active"

    //when & then
    buttons(nextSelectPage).props.onClick(event.asInstanceOf[js.Any])
    pages(props.selectedPage).props.className shouldBe js.undefined
    pages(nextSelectPage).props.className shouldBe "active"
  }

  it should "reset selectedPage if page props changed when update" in {
    //given
    val prevProps = PaginationPanelProps(5)
    val renderer = createTestRenderer(<(PaginationPanel())(^.wrapped := prevProps)())
    (findComponents(renderer.root, <.a.name), findComponents(renderer.root, <.li.name)) match {
      case (buttons, pages) =>
        buttons.size shouldBe (prevProps.totalPages + 2)
        pages.size shouldBe (prevProps.totalPages + 2)
        pages(prevProps.selectedPage).props.className shouldBe "active"
    }

    val props = prevProps.copy(totalPages = 10, selectedPage = 5)

    //when
    TestRenderer.act { () =>
      renderer.update(<(PaginationPanel())(^.wrapped := props)())
    }

    //then
    (findComponents(renderer.root, <.a.name), findComponents(renderer.root, <.li.name)) match {
      case (buttons, pages) =>
        buttons.length shouldBe (props.totalPages - 1)
        pages.length shouldBe (props.totalPages - 1)
        pages(4).props.className shouldBe "active"
    }
  }

  it should "not reset selectedPage if page props not changed when update" in {
    //given
    val prevProps = PaginationPanelProps(5)
    val renderer = createTestRenderer(<(PaginationPanel())(^.wrapped := prevProps)())
    val nextSelectPage = 2
    val event = mock[MouseSyntheticEventMock]
    (findComponents(renderer.root, <.a.name), findComponents(renderer.root, <.li.name)) match {
      case (buttons, pages) =>
        (event.preventDefault _).expects()
        buttons(nextSelectPage).props.onClick(event.asInstanceOf[js.Any])
        buttons.length shouldBe (prevProps.totalPages + 2)
        pages.length shouldBe (prevProps.totalPages + 2)
        pages(nextSelectPage).props.className shouldBe "active"
    }

    val props = prevProps.copy(alignment = PaginationAlignment.Right)
    props should not be prevProps

    //when
    TestRenderer.act { () =>
      renderer.update(<(PaginationPanel())(^.wrapped := props)())
    }

    //then
    (findComponents(renderer.root, <.a.name), findComponents(renderer.root, <.li.name)) match {
      case (buttons, pages) =>
        buttons.length shouldBe (prevProps.totalPages + 2)
        pages.length shouldBe (prevProps.totalPages + 2)
        pages(nextSelectPage).props.className shouldBe "active"
    }
  }

  it should "render component" in {
    //given
    val props = PaginationPanelProps(5)
    val comp = <(PaginationPanel())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertPaginationPanel(result, props)
  }

  it should "render component with pre-selected page" in {
    //given
    val props = PaginationPanelProps(10, selectedPage = 5)
    val comp = <(PaginationPanel())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertPaginationPanel(result, props)
  }

  it should "return selected range when getSelectedRange" in {
    //when & then
    getSelectedRange(1, 1) shouldBe (1 to 1)
    getSelectedRange(1, 2) shouldBe (1 to 2)
    getSelectedRange(1, 3) shouldBe (1 to 3)
    getSelectedRange(1, 4) shouldBe (1 to 4)
    getSelectedRange(1, 5) shouldBe (1 to 5)

    getSelectedRange(1, 5) shouldBe (1 to 5)
    getSelectedRange(2, 5) shouldBe (1 to 5)
    getSelectedRange(3, 5) shouldBe (1 to 5)
    getSelectedRange(4, 5) shouldBe (1 to 5)
    getSelectedRange(5, 5) shouldBe (1 to 5)

    getSelectedRange(1, 6) shouldBe (1 to 5)
    getSelectedRange(2, 6) shouldBe (1 to 5)
    getSelectedRange(3, 6) shouldBe (1 to 5)
    getSelectedRange(4, 6) shouldBe (2 to 6)
    getSelectedRange(5, 6) shouldBe (2 to 6)
    getSelectedRange(6, 6) shouldBe (2 to 6)

    getSelectedRange(1, 10) shouldBe (1 to 5)
    getSelectedRange(2, 10) shouldBe (1 to 5)
    getSelectedRange(3, 10) shouldBe (1 to 5)
    getSelectedRange(4, 10) shouldBe (2 to 6)
    getSelectedRange(5, 10) shouldBe (3 to 7)
    getSelectedRange(6, 10) shouldBe (4 to 8)
    getSelectedRange(7, 10) shouldBe (5 to 9)
    getSelectedRange(8, 10) shouldBe (6 to 10)
    getSelectedRange(9, 10) shouldBe (6 to 10)
    getSelectedRange(10, 10) shouldBe (6 to 10)
  }

  it should "return totalPages for the given totalCount and limit when toTotalPages" in {
    //when & then
    toTotalPages(0, 1) shouldBe 1
    toTotalPages(0, 10) shouldBe 1
    toTotalPages(1, 10) shouldBe 1
    toTotalPages(2, 10) shouldBe 1
    toTotalPages(9, 10) shouldBe 1
    toTotalPages(10, 10) shouldBe 1
    toTotalPages(11, 10) shouldBe 2
    toTotalPages(12, 10) shouldBe 2
    toTotalPages(19, 10) shouldBe 2
    toTotalPages(20, 10) shouldBe 2
    toTotalPages(21, 10) shouldBe 3
  }

  it should "return offset for the given page and limit when toOffset" in {
    //when & then
    toOffset(1, 1) shouldBe 0
    toOffset(1, 10) shouldBe 0
    toOffset(2, 1) shouldBe 1
    toOffset(2, 2) shouldBe 2
    toOffset(2, 3) shouldBe 3
    toOffset(2, 10) shouldBe 10
    toOffset(3, 10) shouldBe 20
  }

  it should "return page for the given offset and limit when toPage" in {
    //when & then
    toPage(0, 1) shouldBe 1
    toPage(1, 1) shouldBe 2
    toPage(2, 1) shouldBe 3
    toPage(0, 2) shouldBe 1
    toPage(1, 2) shouldBe 1
    toPage(2, 2) shouldBe 2
    toPage(3, 2) shouldBe 2
    toPage(4, 2) shouldBe 3
    toPage(0, 10) shouldBe 1
    toPage(9, 10) shouldBe 1
    toPage(10, 10) shouldBe 2
    toPage(11, 10) shouldBe 2
    toPage(19, 10) shouldBe 2
    toPage(20, 10) shouldBe 3
    toPage(21, 10) shouldBe 3
  }

  private def assertPaginationPanel(result: TestInstance, props: PaginationPanelProps): Unit = {
    def renderBtn(text: String, attributes: Any*): Element = {
      <.li(attributes: _*)(
        <.a(^.href := "")(text)
      )
    }

    def pageBtn(text: String, page: Int, disableable: Boolean): Element = {
      val attributes =
        if (page == props.selectedPage) {
          Some(^.className := (if (disableable) "disabled" else "active"))
        }
        else None

      renderBtn(text, attributes)
    }

    def scrollBtn(text: String): Element = renderBtn(text, None)

    val minPage = 1
    val maxPage = props.totalPages
    val selectedRange = getSelectedRange(props.selectedPage, maxPage)
    val buttons = selectedRange.map { page =>
      Some(pageBtn(s"$page", page, disableable = false))
    }

    assertNativeComponent(result, <.div(^.className := props.alignment.style)(), { case List(ul) =>
      assertNativeComponent(ul, <.ul()(
        pageBtn("<<", minPage, disableable = true),
        if (minPage < selectedRange.start) {
          Some(scrollBtn("<"))
        }
        else None,
        buttons,
        if (maxPage > selectedRange.end) {
          Some(scrollBtn(">"))
        }
        else None,
        pageBtn(">>", maxPage, disableable = true)
      ))
    })
  }
}

object PaginationPanelSpec {

  @JSExportAll
  trait MouseSyntheticEventMock {

    def preventDefault(): Unit
  }
}
