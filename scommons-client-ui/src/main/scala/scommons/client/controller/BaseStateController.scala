package scommons.client.controller

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import scommons.client.ui.UiComponent

trait BaseStateController[S, P] {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced { dispatch: Dispatch =>
    (state: S, props: Props[Unit]) =>
      mapStateToProps(dispatch, state, props)
  }(uiComponent.reactClass)
  
  def uiComponent: UiComponent[P]

  def mapStateToProps(dispatch: Dispatch, state: S, props: Props[Unit]): P
}
