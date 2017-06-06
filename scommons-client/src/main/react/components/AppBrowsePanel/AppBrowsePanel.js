const React = require('react')
const styles = require('./AppBrowsePanel.css')
const BrowseTreePanel = require('../BrowseTreePanel/BrowseTreePanel')

class AppBrowsePanel extends React.Component {
  render() {
    return (
      <div className="row-fluid">
        <div className="span4">
          <div className="well sidebar-nav">
            {//<ButtonsPanel ui:field="buttonsPanel" className={style.sidebar-bp} />
            }
            <BrowseTreePanel/>
          </div>
        </div>
        <div className="span8" />
      </div>
    )
  }
}

module.exports = AppBrowsePanel
