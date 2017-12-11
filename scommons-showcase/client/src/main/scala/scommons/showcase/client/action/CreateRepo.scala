package scommons.showcase.client.action

import scommons.client.task.FutureTask
import scommons.showcase.api.repo.RepoResp

case class CreateRepo(task: FutureTask[RepoResp]) extends TaskAction
