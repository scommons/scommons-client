package scommons.client.util

case class BrowsePath private(value: String, prefix: String, exact: Boolean) {
  
  require(!value.isEmpty, "BrowsePath should not be empty!")
  require(value.startsWith("/"), s"BrowsePath should start with '/', path: $value")
  require(value.startsWith(prefix), s"BrowsePath should start with prefix, path: $value, prefix: $prefix")

  def matches(path: String): Boolean = {
    if (exact) path == value
    else path.startsWith(prefix)
  }
  
  override def toString: String = value
}

object BrowsePath {

  def apply(path: String, exact: Boolean = true): BrowsePath = {
    BrowsePath(path, path, exact)
  }
  
  def apply(path: String, prefix: String): BrowsePath = {
    BrowsePath(path, prefix, exact = false)
  }
}
