package scommons.client.task

import scommons.client.task.AbstractTask.AbstractTaskKey
import scommons.client.util.Identity

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

trait AbstractTask {

  lazy val key: AbstractTaskKey = Identity(this)

  def message: String

  def startTime: Long

  def onComplete(f: Try[_] => Unit): Unit
}

object AbstractTask {

  type AbstractTaskKey = Identity[AbstractTask]
}

case class FutureTask(message: String, future: Future[_]) extends AbstractTask {

  val startTime: Long = System.currentTimeMillis()

  def onComplete(f: (Try[_]) => Unit): Unit = future.onComplete(f)
}
