package scommons.showcase.client

import org.scalajs.dom.document

import scala.scalajs.js.JSApp

object Main extends JSApp {

  def main(): Unit = {
    val paragraph = document.createElement("p")
    paragraph.innerHTML = "<strong>It works!</strong>"

    document.getElementById("root").appendChild(paragraph)
  }
}
