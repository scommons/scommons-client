const React = require('react')
const styles = require('./AppMainPanel.css')
const Header = require('../Header/Header')
const Footer = require('../Footer/Footer')

class AppMainPanel extends React.Component {
  render() {
    return (
      <div>
        <Header/>
        <div className="container-fluid">
          {this.props.children}
        </div>
        <Footer/>
      </div>
    )
  }
}

module.exports = AppMainPanel
