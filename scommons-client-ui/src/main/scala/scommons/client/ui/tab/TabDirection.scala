package scommons.client.ui.tab

sealed trait TabDirection {

  def style: String
}

object TabDirection {

  case object Top extends TabDirection {
    val style = "tabbable"
  }

  case object Bottom extends TabDirection {
    val style = "tabbable tabs-below"
  }

  case object Left extends TabDirection {
    val style = "tabbable tabs-left"
  }

  case object Right extends TabDirection {
    val style = "tabbable tabs-right"
  }
}
