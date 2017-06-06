const React = require('react')
const styles = require('./Header.css')

class Header extends React.Component {
  render() {
    return (
      <div className="navbar navbar-inverse navbar-fixed-top">
        <div className="navbar-inner">
          <div className="container-fluid">
            <button type="button" className="btn btn-navbar"
              data-toggle="collapse" data-target=".nav-collapse">
              <span className="icon-bar"></span>
              <span className="icon-bar"></span>
              <span className="icon-bar"></span>
            </button>
            <a className="brand" href="#">
              App
            </a>
            <div className="nav-collapse collapse">
              <ul className="nav pull-right">
                <li className="dropdown">
                  <a className="dropdown-toggle" data-toggle="dropdown" href="#">
                      <span>user</span>
                      <b className="caret"></b>
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

module.exports = Header
