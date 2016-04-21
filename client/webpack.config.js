var path, config, webpack, autoprefixer;

path = require('path');
webpack = require('webpack');

const NODE_ENV = process.env.NODE_ENV || 'DEVELOP';

config = {
	devtool: 'cheap-module-eval-source-map',
	entry: getEntry(),

	output: {
		path: path.resolve(__dirname, 'dist'),
		publicPath: '/',
		filename: 'bundle.js'
	},

	plugins: getPlugins(),

	module: {
		loaders: [
			{
				test: /\.jsx?$/,
				exclude: /(node_modules|bower_components)/,
				loader: 'babel',
				query: {
					presets: ['es2015', 'stage-0', 'react']
				},
				plugins: ['transform-runtime']
			}
		]
	},

	postcss: function () {
		return [autoprefixer, nesting, customProperties];
	},

	devServer: {
		contentBase: __dirname + '/dist',
		host: 'localhost',
		port: 1337,
		hot: true
	}
};

function getEntry() {
	var variant = {
		"DEVELOP": ['webpack-dev-server/client?http://localhost:1337', 'webpack/hot/dev-server', './app/index.js'],
		"PRODUCTION": ['./app/index.js' ]
	};
	return variant[NODE_ENV];
}

function getPlugins() {
	var variant = {
		"DEVELOP": [
			new webpack.optimize.OccurenceOrderPlugin(),
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
	return variant[NODE_ENV];
}

if(NODE_ENV == 'PRODUCTION') {
	config.plugins.push(
	)
}

module.exports = config;
console.log(config.plugins)
console.log(config.entry)
