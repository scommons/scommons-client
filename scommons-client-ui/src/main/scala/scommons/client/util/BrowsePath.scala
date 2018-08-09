package scommons.client.util

class BrowsePath private(val value: String) extends AnyVal {

  override def toString: String = value
}

object BrowsePath {

  def apply(path: String): BrowsePath = {
    require(!path.isEmpty, "BrowsePath should not be empty!")
    require(path.startsWith("/"), s"BrowsePath should start with '/', path: $path")
    
    new BrowsePath(path)
  }
}
