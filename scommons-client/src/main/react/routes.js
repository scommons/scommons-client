const React = require('react')
const {
  Router,
  Route,
  IndexRoute,
  browserHistory
} = require('react-router')

const AppMainPanel = require('components/AppMainPanel/AppMainPanel')
const AppBrowsePanel = require('components/AppBrowsePanel/AppBrowsePanel')
//const { BootstrapTable, TableHeaderColumn } = require('react-bootstrap-table')

module.exports = (
  <Router history={browserHistory}>
    <Route path="/" component={AppMainPanel}>
      <IndexRoute component={AppBrowsePanel} />
    </Route>
  </Router>
)
