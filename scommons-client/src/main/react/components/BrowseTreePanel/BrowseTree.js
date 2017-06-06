const React = require('react')
const styles = require('./BrowseTree.css')
const BrowseTreeNode = require('./BrowseTreeNode')

const BrowseTree = React.createClass({

  propTypes: {
    levels: React.PropTypes.number,

    expandIcon: React.PropTypes.string,
    collapseIcon: React.PropTypes.string,
    emptyIcon: React.PropTypes.string,
    nodeIcon: React.PropTypes.string,

    color: React.PropTypes.string,
    backColor: React.PropTypes.string,
    borderColor: React.PropTypes.string,
    onhoverColor: React.PropTypes.string,
    selectedColor: React.PropTypes.string,
    selectedBackColor: React.PropTypes.string,

    enableLinks: React.PropTypes.bool,
    highlightSelected: React.PropTypes.bool,
    showBorder: React.PropTypes.bool,
    showTags: React.PropTypes.bool,

    nodes: React.PropTypes.arrayOf(React.PropTypes.object),
    data: React.PropTypes.arrayOf(React.PropTypes.object)
  },

  getDefaultProps: function () {
    return {
      levels: 2,

      expandIcon: 'glyphicon glyphicon-plus',
      collapseIcon: 'glyphicon glyphicon-minus',
      emptyIcon: 'glyphicon',
      nodeIcon: 'glyphicon glyphicon-stop',

      color: undefined,
      backColor: undefined,
      borderColor: undefined,
      onhoverColor: '#F5F5F5',
      selectedColor: '#FFFFFF',
      selectedBackColor: '#428bca',

      enableLinks: false,
      highlightSelected: true,
      showBorder: true,
      showTags: false,

      nodes: []
    }
  },

  setNodes: function(node) {
    if (!node.nodes) {
        return;
    }

    const self = this
    node.nodes.forEach(function(node) {
      self.props.nodes.push(node)
      self.setNodes(node)
    })
  },

  render: function() {
    const data = this.props.data
    this.setNodes({
        nodes: data
    })

    var children = []
    if (data) {
      const self = this
      children = data.map((node, index) => (
        <BrowseTreeNode node={node}
          key={index}
          level={1}
          visible={true}
          options={self.props} />
      ))
    }

    return (
      <div>
        {children}
      </div>
    )
  }
})

module.exports = BrowseTree
