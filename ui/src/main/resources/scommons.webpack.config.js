const MiniCssExtractPlugin = require('mini-css-extract-plugin')

module.exports = {
  stats: {
    children: false // disable child plugin/loader logging
  },
  
  module: {
    rules: [{
      test: /\.(ico|png|gif|jpe?g|svg)$/i,
      use: {
        loader: 'url-loader',
        options: {
          esModule: false
          //limit: 8192
        }
      },
      exclude: /node_modules/
    }, {
      test: /\.css$/,
      use: [{
        loader: MiniCssExtractPlugin.loader,
        options: {
          esModule: false
        }
      }, {
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
