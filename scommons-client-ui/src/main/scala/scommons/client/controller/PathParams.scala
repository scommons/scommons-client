package scommons.client.controller

import scala.util.matching.Regex

class PathParams private(val path: String) extends AnyVal {

  def extractInt(regex: Regex, exact: Boolean = false): Option[Int] = {
    if (exact && !regex.pattern.matcher(path).matches()) {
      None
    }
    else {
      for {
        regex(id) <- regex.findPrefixMatchOf(path)
      } yield {
        id.toInt
      }
    }
  }
}

object PathParams {
  
  def apply(path: String): PathParams = new PathParams(path)
}
