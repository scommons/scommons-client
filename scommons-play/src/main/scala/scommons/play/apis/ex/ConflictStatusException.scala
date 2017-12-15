package scommons.play.apis.ex

import scommons.api.ApiStatus

class ConflictStatusException(status: ApiStatus) extends StatusException(status)
