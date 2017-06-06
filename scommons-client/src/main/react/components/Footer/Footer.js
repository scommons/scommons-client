const React = require('react')
const styles = require('./Footer.css')

class Footer extends React.Component {
  render() {
    const {
      copyright,
      version
    } = this.props

    return (
      <div>
        <hr />
        <footer>
          <p className="text-center">
              <span>{copyright ? copyright : "copyright"}</span>&nbsp;
              <small>{version ? version : "version"}</small>
          </p>
        </footer>
      </div>
    )
  }
}

module.exports = Footer
