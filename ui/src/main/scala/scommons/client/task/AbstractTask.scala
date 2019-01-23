package scommons.client.task

import scommons.client.task.AbstractTask.AbstractTaskKey
import scommons.client.util.Identity

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.Try

trait AbstractTask {

  lazy val key: AbstractTaskKey = Identity(this)

  val startTime: Long = System.currentTimeMillis()

  def message: String

  def onComplete(f: Try[_] => Unit): Unit
}

object AbstractTask {

  type AbstractTaskKey = Identity[AbstractTask]
}

case class FutureTask[T](message: String, future: Future[T]) extends AbstractTask {

  def onComplete(f: (Try[_]) => Unit): Unit = future.onComplete(f)
}
