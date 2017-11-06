package scommons.client.app

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.util.ActionsData

case class AppBrowseData(path: String,
                         actions: ActionsData,
                         reactClass: Option[ReactClass])
