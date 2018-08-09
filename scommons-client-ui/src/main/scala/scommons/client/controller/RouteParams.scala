package scommons.client.controller

import io.github.shogowada.scalajs.reactjs.router.RouterProps.RouterProps
import scommons.client.util.BrowsePath

class RouteParams(routerProps: RouterProps) {

  def path: BrowsePath = BrowsePath(routerProps.location.pathname)
}
