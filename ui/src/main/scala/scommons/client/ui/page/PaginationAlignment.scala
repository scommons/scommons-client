package scommons.client.ui.page

sealed trait PaginationAlignment {

  def style: String
}

object PaginationAlignment {

  case object Left extends PaginationAlignment {
    val style = "pagination"
  }

  case object Centered extends PaginationAlignment {
    val style = "pagination pagination-centered"
  }

  case object Right extends PaginationAlignment {
    val style = "pagination pagination-right"
  }
}
