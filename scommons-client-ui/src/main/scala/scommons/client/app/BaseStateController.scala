package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch

trait BaseStateController[S, P] {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced { dispatch: Dispatch =>
    (state: S, props: Props[Unit]) =>
      mapStateToProps(dispatch, state, props)
  }(component)
  
  def component: ReactClass

  def mapStateToProps(dispatch: Dispatch, state: S, props: Props[Unit]): P
}
