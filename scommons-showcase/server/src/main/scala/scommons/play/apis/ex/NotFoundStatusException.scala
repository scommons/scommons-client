package scommons.play.apis.ex

import scommons.api.ApiStatus

class NotFoundStatusException(status: ApiStatus) extends StatusException(status)
