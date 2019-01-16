const ExtractTextPlugin = require('extract-text-webpack-plugin')

module.exports = {
  module: {
    rules: [{
      test: /\.(ico|png|gif|jpe?g|svg)$/i,
      loader: 'url-loader',
      options: {
        //limit: 8192
      },
      exclude: /node_modules/
    }, {
      test: /\.css$/,
      use: ExtractTextPlugin.extract({
        fallback: 'style-loader',
        use: [{
          loader: 'css-loader',
          options: {
            modules: true,
            localIdentName: '[local]__[hash:base64:5]',
            sourceMap: true
          }
        }, {
          loader: 'resolve-url-loader',
          options: {
            sourceMap: true
          },
        }]
      }),
      exclude: /node_modules/
    }]
  },

  resolve: {
    modules: [
      './node_modules',
      '.'
    ]
  },

  plugins: [
    new ExtractTextPlugin('styles/[name].css')
  ]
}
