package scommons.play.apis.ex

import scommons.api.ApiStatus

class InternalServerErrorStatusException(status: ApiStatus) extends StatusException(status)
