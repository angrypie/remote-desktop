var config;

var path = require('path');
var webpack = require('webpack');


const NODE_ENV = process.env.NODE_ENV || 'DEVELOP';

config = {
	devtool: getDevtool(),
	entry: getEntry(),

	output: {
		path: path.resolve(__dirname, 'dist'),
		publicPath: '/',
		filename: 'bundle.js'
	},

	resolve: {
		root: path.resolve(__dirname, 'app'),
		extensions: ['', '.js']
	},

	plugins: getPlugins(),

	module: {
		loaders: [
			// js babel
			{
				test: /\.jsx?$/,
				exclude: /(node_modules|bower_components)/,
				loader: 'babel',
				query: {
					presets: ['es2015', 'stage-0', 'react']
				},
				plugins: ['transform-runtime']
			},
			// css
			{
				test: /\.css$/,
				loaders: ["style", "css?modules&importLoaders=1&localIdentName=[name]__[local]__[hash:base64:5]", "postcss"]
			}
		]
	},

	postcss: function () {
		return [];
	},

	devServer: {
		contentBase: __dirname + '/dist',
		host: 'localhost',
		port: 1337,
		hot: true
	}
};

function getDevtool() {
	var variant = {
		"DEVELOP": 'cheap-module-eval-source-map',
		"PRODUCTION": ''
	};
	return variant[NODE_ENV];
}

function getEntry() {
	var variant = {
		"DEVELOP": ['webpack-dev-server/client?http://localhost:1337', 'webpack/hot/dev-server', './app/index.js'],
		"PRODUCTION": ['./app/index.js' ]
	};
	return variant[NODE_ENV];
}

function getPlugins() {
	var base = [
			new webpack.DefinePlugin({
				NODE_ENV: JSON.stringify(NODE_ENV)
			}),
			new webpack.optimize.OccurenceOrderPlugin(),
	]
	var variant = {
		"DEVELOP": [
	    new webpack.HotModuleReplacementPlugin(),
	    new webpack.NoErrorsPlugin()
		],
		"PRODUCTION": [
			new webpack.optimize.UglifyJsPlugin({
				compress: {
					warnings: false,
					drop_console: true,
					unsafe: true
				}
			})
		]
	};
	return base.concat(variant[NODE_ENV]);
}

if(NODE_ENV == 'PRODUCTION') {
	config.plugins.push(
	)
}

module.exports = config;
