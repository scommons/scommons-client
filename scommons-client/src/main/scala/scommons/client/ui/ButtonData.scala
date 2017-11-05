package scommons.client.ui

sealed trait ButtonData {

  def command: String
}

case class ImageButtonData(command: String,
                           image: String,
                           disabledImage: String,
                           text: String,
                           primary: Boolean = false) extends ButtonData
