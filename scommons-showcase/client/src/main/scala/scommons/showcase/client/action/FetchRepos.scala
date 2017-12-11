package scommons.showcase.client.action

import scommons.client.task.FutureTask
import scommons.showcase.api.repo.RepoListResp

case class FetchRepos(task: FutureTask[RepoListResp]) extends TaskAction
