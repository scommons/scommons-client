const React = require('react')
const styles = require('./BrowseTree.css')
const BrowseTree = require('./BrowseTree')
const BrowseTreeNode = require('./BrowseTreeNode')

class BrowseTreePanel extends React.Component {
  render() {
    const data = [{
      text: "BrowseTree Parent 1",
      icon: "glyphicon glyphicon-stop",
      tags: ['available'],
      nodes: [{
        text: "Child"
      }]
    }]

    return (
      <div className={styles.browseTree}>
        <div className={`
            ${styles.browseTreeSelectedItem}
            ${styles.browseTreeTopItem}
            ${styles.browseTreeItem}
          `}>
          <div className={`
              ${styles.browseTreeNode}
              ${styles.browseTreeTopItemImageValue}
              ${styles.browseTreeItem}
            `}>
            <div className={`
                ${styles.browseTreeNodeIcon}
              `}>
              <div className={`
                  ${styles.browseTreeClosedArrow}
                `}
              />
            </div>
            <div className={`
                ${styles.browseTreeItemValue}
              `}>
              Test Parent
            </div>
          </div>
        </div>

        <div className={`
          ${styles.browseTreeItem}
          ${styles.browseTreeTopItem}
        `}>Test Parent2</div>

        <BrowseTree data={data} showTags={true} />
      </div>
    )
  }
}

module.exports = BrowseTreePanel
