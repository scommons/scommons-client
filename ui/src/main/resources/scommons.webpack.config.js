const MiniCssExtractPlugin = require('mini-css-extract-plugin')

module.exports = {
  stats: {
    children: false // disable child plugin/loader logging
  },
  
  module: {
    rules: [{
      test: /\.(ico|png|gif|jpe?g|svg)$/i,
      // More information here https://webpack.js.org/guides/asset-modules/
      type: "asset",
      exclude: /node_modules/
    }, {
      test: /\.css$/i,
      use: [{
        loader: MiniCssExtractPlugin.loader,
        options: {
          esModule: false
        }
      }, {
        loader: 'css-loader',
        options: {
          modules: true,
          sourceMap: true,
          modules: {
            localIdentName: '[local]__[hash:base64:5]'
          }
        }
      }],
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
    new MiniCssExtractPlugin({
      filename: 'styles/[name].css'
    })
  ]
}
