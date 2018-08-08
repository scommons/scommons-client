package scommons.client.controller

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps.RouterProps

trait BaseStateAndRouteController[S, P] extends BaseStateController[S, P] {

  override def mapStateToProps(dispatch: Dispatch, state: S, props: Props[Unit]): P = {
    mapStateAndRouteToProps(dispatch, state, props, new RouterProps(props))
  }

  def mapStateAndRouteToProps(dispatch: Dispatch,
                              state: S,
                              props: Props[Unit],
                              routerProps: RouterProps): P
}
