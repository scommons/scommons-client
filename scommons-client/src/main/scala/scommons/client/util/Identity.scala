package scommons.client.util

final case class Identity[+A <: AnyRef](obj: A) {

  override def equals(that: Any): Boolean = that match {
    case that: Identity[_] => this.obj eq that.obj
    case _ => false
  }

  override def hashCode(): Int =
    System.identityHashCode(obj)
}
