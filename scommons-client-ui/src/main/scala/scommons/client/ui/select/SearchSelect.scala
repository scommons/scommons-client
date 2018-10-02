package scommons.client.ui.select

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.scalajs.dom
import scommons.client.ui.UiComponent

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}

case class SearchSelectProps(initialValue: Option[SelectData],
                             onLoad: String => Future[List[SelectData]] = _ => Future.successful(Nil),
                             onChange: SelectData => Unit = _ => ())

object SearchSelect extends UiComponent[SearchSelectProps] {

  private case class SearchSelectState(isLoading: Boolean = false,
                                       value: String = "",
                                       handleId: Option[Int] = None,
                                       options: List[SelectData] = Nil)
  
  def apply(): ReactClass = reactClass
  lazy val reactClass: ReactClass = createComp

  private def createComp: ReactClass = React.createClass[PropsType, SearchSelectState](
    getInitialState = { _ =>
      SearchSelectState()
    },
    render = { self =>
      val props = self.props.wrapped
  
      <(Select())(^.wrapped := SelectProps(
        selectedOptions = props.initialValue.toList,
        options = self.state.options,
        onSelectChange = props.onChange,
        isLoading = self.state.isLoading,
        onInputChange = { value =>
          self.state.handleId.foreach { handleId =>
            dom.window.clearTimeout(handleId)
          }

          var handleId = 0
          handleId = dom.window.setTimeout({ () =>
            dom.window.clearTimeout(handleId)
            self.setState(s => s.copy(isLoading = true, handleId = None))
            
            val loadValue = self.state.value
            props.onLoad(loadValue).onComplete {
              case Success(list) if self.state.value == loadValue =>
                self.setState(s => s.copy(isLoading = false, options = list))
              case Failure(_) if self.state.value == loadValue =>
                self.setState(s => s.copy(isLoading = false))
              case _ =>
            }
          }, 750.millis.toMillis.toDouble)
          
          self.setState(s => s.copy(value = value, handleId = Some(handleId)))
        }
      ))()
    }
  )
}
