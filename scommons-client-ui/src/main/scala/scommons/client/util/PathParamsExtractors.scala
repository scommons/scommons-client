package scommons.client.util

import scala.util.matching.Regex

trait PathParamsExtractors {

  def extractId(idRegex: Regex, path: String): Option[Int] = {
    for {
      idRegex(id) <- idRegex.findPrefixMatchOf(path)
    } yield {
      id.toInt
    }
  }
}

object PathParamsExtractors extends PathParamsExtractors
