const React = require('react')
const styles = require('./BrowseTree.css')

const BrowseTreeNode = React.createClass({

  getInitialState: function() {
    const node = this.props.node
    return {
      expanded: (node.state && node.state.hasOwnProperty('expanded')) ?
                  node.state.expanded : (this.props.level < this.props.options.levels) ?
                    true : false,
      selected: (node.state && node.state.hasOwnProperty('selected')) ?
                  node.state.selected :
                  false
    }
  },

  toggleExpanded: function(node, event) {
    this.setState({
        expanded: !this.state.expanded
    })

    event.stopPropagation()
  },

  toggleSelected: function(node, event) {
    this.setState({
        selected: !this.state.selected
    })

    event.stopPropagation()
  },

  render: function() {
    const node = this.props.node
    const options = this.props.options

    var style
    if (!this.props.visible) {
      style = {
        display: 'none'
      }
    }
    else {
      if (options.highlightSelected && this.state.selected) {
        style = {
          color: options.selectedColor,
          backgroundColor: options.selectedBackColor
        }
      }
      else {
        style = {
          color: node.color || options.color,
          backgroundColor: node.backColor || options.backColor
        }
      }

      if (!options.showBorder) {
        style.border = 'none'
      }
      else if (options.borderColor) {
        style.border = '1px solid ' + options.borderColor
      }
    }

    var indents = []
    for (var i = 0; i < this.props.level-1; i++) {
      indents.push(<span key={i}></span>)
    }

    var expandCollapseIcon
    if (node.nodes) {
      if (!this.state.expanded) {
        expandCollapseIcon = (
          <span className={options.expandIcon}
                onClick={this.toggleExpanded.bind(this, node)}>
          </span>
        )
      }
      else {
        expandCollapseIcon = (
          <span className={options.collapseIcon}
                onClick={this.toggleExpanded.bind(this, node)}>
          </span>
        )
      }
    }
    else {
      expandCollapseIcon = (
        <span className={options.emptyIcon}></span>
      )
    }

    var nodeIcon = (
      <span>
        <i className={node.icon || options.nodeIcon}></i>
      </span>
    )

    var nodeText
    if (options.enableLinks) {
      nodeText = (
        <a href={node.href} /*style="color:inherit;"*/>
          {node.text}
        </a>
      )
    }
    else {
      nodeText = (
        <span>{node.text}</span>
      )
    }

    var badges
    if (options.showTags && node.tags) {
      badges = node.tags.map((tag, index) => (
        <span className='badge' key={index}>{tag}</span>
      ))
    }

    var children = []
    if (node.nodes) {
      const self = this
      children = node.nodes.map((node, index) => (
        <BrowseTreeNode node = {node}
          key={index}
          level={self.props.level+1}
          visible={self.state.expanded && self.props.visible}
          options={options} />
      ))
    }

    return (
      <div style={style}
          onClick={this.toggleSelected.bind(this, node)}>
        {indents}
        {expandCollapseIcon}
        {nodeIcon}
        {nodeText}
        {badges}
        {children}
      </div>
    )
  }
})

module.exports = BrowseTreeNode
