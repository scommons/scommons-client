package scommons.play.apis.ex

import scommons.api.ApiStatus

class BadRequestStatusException(status: ApiStatus) extends StatusException(status)
