package scommons.client.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import scommons.client.app.{AppBrowseController, AppBrowseControllerProps}
import scommons.client.ui.Buttons
import scommons.react.UiComponent
import scommons.react.redux.BaseStateController

object ShowcaseRouteController extends BaseStateController[ShowcaseState, AppBrowseControllerProps] {

  lazy val uiComponent: UiComponent[AppBrowseControllerProps] = AppBrowseController

  def mapStateToProps(dispatch: Dispatch, state: ShowcaseState, props: Props[Unit]): AppBrowseControllerProps = {
    AppBrowseControllerProps(
      List(Buttons.REFRESH, Buttons.ADD, Buttons.REMOVE, Buttons.EDIT),
      ShowcaseReducer.getTreeRoots(state),
      dispatch,
      Set(ShowcaseReducer.widgetsNode.path)
    )
  }
}
