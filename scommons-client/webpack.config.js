const path = require('path')
const ExtractTextPlugin = require('extract-text-webpack-plugin')

module.exports = {
  entry: {
    index: [
      'babel-polyfill',
      './src/main/react/index.js'
    ]
  },
  output: {
    path: path.join(__dirname, 'build'),
    filename: '[name].js'
  },
  target: 'web',
  module: {
    loaders: [{
      loader: 'babel-loader',
      include: [path.resolve(__dirname, 'src/main/react')],
      exclude: /node_modules/,
      test: /\.js$/,
      query: {
        presets: ['react', 'es2015', 'stage-0']
      }
    }, {
      loader: 'json-loader',
      test: /\.json$/
    }, {
      loader: "file-loader",
      test: /\.(ico|png|gif|jpe?g|svg)$/i
    }, {
      loader: ExtractTextPlugin.extract('style-loader', 'css-loader?modules&localIdentName=[local]__[hash:base64:5]&sourceMap!resolve-url-loader?sourceMap'),
      test: /\.css$/,
      exclude: /node_modules/
    }]
  },
  resolve: {
    modulesDirectories: [
      './node_modules',
      './src/main/react'
    ]
  },
  plugins: [
    new ExtractTextPlugin('styles.css')
  ]
}
