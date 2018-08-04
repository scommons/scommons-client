package scommons.client.util

import scala.util.matching.Regex

trait PathParamsExtractors {

  def extractId(idRegex: Regex, path: String, exact: Boolean = false): Option[Int] = {
    if (exact && !idRegex.pattern.matcher(path).matches()) {
      None
    }
    else {
      for {
        idRegex(id) <- idRegex.findPrefixMatchOf(path)
      } yield {
        id.toInt
      }
    }
  }
}

object PathParamsExtractors extends PathParamsExtractors
