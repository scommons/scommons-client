package scommons.client.util

case class BrowsePath(value: String, exact: Boolean = true) {
  
  require(!value.isEmpty, "BrowsePath should not be empty!")
  require(value.startsWith("/"), s"BrowsePath should start with '/', path: $value")

  def matches(path: String): Boolean = {
    if (exact) path == value
    else path.startsWith(value)
  }
  
  override def toString: String = value
}
