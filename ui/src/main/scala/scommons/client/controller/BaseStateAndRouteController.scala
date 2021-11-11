package scommons.client.controller

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import scommons.react.redux._

trait BaseStateAndRouteController[S, P] extends BaseStateController[S, P] {

  override def mapStateToProps(dispatch: Dispatch, state: S, props: Props[Unit]): P = {
    mapStateAndRouteToProps(dispatch, state, new RouteParams(props))
  }

  def mapStateAndRouteToProps(dispatch: Dispatch,
                              state: S,
                              routeParams: RouteParams): P
}
