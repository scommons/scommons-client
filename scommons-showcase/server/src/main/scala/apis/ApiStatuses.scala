package apis

import scommons.api.ApiStatus

object ApiStatuses {

  val RepoNotFound = ApiStatus(1001, "Repo not found")
  val RepoAlreadyExists = ApiStatus(1002, "Repo with such name already exists")
}
