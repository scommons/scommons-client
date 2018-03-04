package scommons.client

import org.scalajs.dom
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{Assertion, AsyncFlatSpec}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.scalajs.concurrent.JSExecutionContext
import scala.util.control.NonFatal

trait AsyncTestSpec extends AsyncFlatSpec
  with BaseTestSpec
  with AsyncMockFactory {

  implicit override def executionContext: ExecutionContext = JSExecutionContext.queue

  def eventually(mayFailBlock: => Assertion): Future[Assertion] = {
    var handleId = 0
    var lastError: Throwable = null
    var count = 10

    val promise = Promise[Assertion]()

    handleId = dom.window.setInterval({ () =>
      try {
        count -= 1
        if (count >= 0) {
          lastError = null

          promise.success(mayFailBlock)
        }
        else {
          promise.failure(lastError)
        }

        dom.window.clearInterval(handleId)
      }
      catch {
        case NonFatal(ex) => lastError = ex
      }
    }, 100)

    promise.future
  }
}
