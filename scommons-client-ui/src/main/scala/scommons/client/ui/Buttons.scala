package scommons.client.ui

/**
  * Common buttons definition.
  */
object Buttons {

  val REFRESH = ImageButtonData("refresh", ButtonImagesCss.refresh, ButtonImagesCss.refreshDisabled, "Refresh")

  val ADD = ImageButtonData("add", ButtonImagesCss.add, ButtonImagesCss.addDisabled, "Add")

  val REMOVE = ImageButtonData("remove", ButtonImagesCss.delete, ButtonImagesCss.deleteDisabled, "Remove")

  val EDIT = ImageButtonData("edit", ButtonImagesCss.pencil, ButtonImagesCss.pencilDisabled, "Edit")

  val SAVE = ImageButtonData("save", ButtonImagesCss.disk, ButtonImagesCss.diskDisabled, "Save")

  val FIND = ImageButtonData("find", ButtonImagesCss.find, ButtonImagesCss.find, "Find")

  val OPEN = ImageButtonData("open", ButtonImagesCss.folder, ButtonImagesCss.folderDisabled, "Open")

  val INFO = ImageButtonData("info", ButtonImagesCss.info, ButtonImagesCss.infoDisabled, "Info")

  val PRINT = ImageButtonData("print", ButtonImagesCss.printer, ButtonImagesCss.printerDisabled, "Print")

  val OK = ImageButtonData("ok", ButtonImagesCss.accept, ButtonImagesCss.acceptDisabled, "OK")

  val CANCEL = ImageButtonData("cancel", ButtonImagesCss.cancel, ButtonImagesCss.cancelDisabled, "Cancel")
}
