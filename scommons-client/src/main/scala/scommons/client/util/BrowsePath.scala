package scommons.client.util

class BrowsePath(val value: String) extends AnyVal {

  override def toString: String = value
}

object BrowsePath {

  def apply(value: String): BrowsePath = new BrowsePath(value)
}
