var webpack = require('webpack');

module.exports = {
	entry:'./entry.js',
	output: {
		path: __dirname + '/dist',
		filename: 'bundle.js'
	},
	module: {
		loaders:[
			{
				test:/\.css$/,loader:'style!css'
			},
			{
              test: /\.js$/,
              loader: 'babel',
              exclude: /node_modules/
            }
		]
	}
}