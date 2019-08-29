package scommons.client.showcase.demo

import scommons.client.ui.{HTML, HTMLProps}
import scommons.react._

object HTMLDemo extends FunctionComponent[Unit] {

  protected def render(props: Props): ReactElement = {
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
