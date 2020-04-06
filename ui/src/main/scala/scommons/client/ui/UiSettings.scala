package scommons.client.ui

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}
import scala.util.Try

object UiSettings {

  lazy val imgClearCacheUrl: String = {
    val sett = Try {
      g.scommons.UiSettings.imgClearCacheUrl.asInstanceOf[js.UndefOr[String]]
    }.getOrElse(js.undefined)
    
    sett.getOrElse("")
  }
}
