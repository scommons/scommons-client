package scommons.play.apis.ex

import scommons.api.ApiStatus

abstract class StatusException(val status: ApiStatus) extends RuntimeException(status.toString)
