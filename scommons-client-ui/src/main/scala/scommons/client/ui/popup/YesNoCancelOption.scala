package scommons.client.ui.popup

sealed trait YesNoCancelOption {

  def command: String
}

object YesNoCancelOption {

  case object Yes extends YesNoCancelOption {
    val command = "yes"
  }

  case object No extends YesNoCancelOption {
    val command = "no"
  }

  case object Cancel extends YesNoCancelOption {
    val command = "cancel"
  }
}
