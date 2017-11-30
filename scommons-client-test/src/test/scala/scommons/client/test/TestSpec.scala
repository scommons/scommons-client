package scommons.client.test

import io.github.shogowada.scalajs.reactjs.VirtualDOM
import org.scalatest.{FlatSpec, Matchers}

trait TestSpec extends FlatSpec with Matchers {

  lazy val < : VirtualDOM.VirtualDOMElements = VirtualDOM.<
}
