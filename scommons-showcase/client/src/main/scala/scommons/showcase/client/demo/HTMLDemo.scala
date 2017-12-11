package scommons.showcase.client.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.{HTML, HTMLProps}

object HTMLDemo {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit] { _ =>
    <.div(^.style := Map(
      "position" -> "relative",
      "overflow" -> "auto"
    ))(
      <(HTML())(^.wrapped := HTMLProps(
        "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>" +
          "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>",
        wordWrap = false
      ))()
    )
  }
}
