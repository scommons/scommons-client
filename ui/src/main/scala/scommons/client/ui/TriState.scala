package scommons.client.ui

/**
  * Tri-state.
  *
  * @see http://www.javaspecialists.eu/archive/Issue145.html
  */
sealed trait TriState {
  
  def next: TriState
}

object TriState {

  def apply(isSelected: Boolean): TriState =
    if (isSelected) Selected
    else Deselected
  
  def isSelected(value: TriState): Boolean = value == Selected
  
  case object Selected extends TriState {
    val next: TriState = Deselected
  }
  
  case object Indeterminate extends TriState {
    val next: TriState = Selected
  }
  
  case object Deselected extends TriState {
    val next: TriState = Selected
  }
}
