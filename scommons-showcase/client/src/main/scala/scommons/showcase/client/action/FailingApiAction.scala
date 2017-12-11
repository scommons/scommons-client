package scommons.showcase.client.action

import scommons.api.StatusResponse
import scommons.client.task.FutureTask

case class FailingApiAction(task: FutureTask[StatusResponse]) extends TaskAction
