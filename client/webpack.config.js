var path, config, webpack, autoprefixer;

path = require('path');
webpack = require('webpack');

const NODE_ENV = process.env.NODE_ENV || 'DEVELOP';

config = {
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
					presets: ['react', 'es2015']
				}
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
	var dev = [ 'webpack-dev-server/client?http://localhost:1337', 'webpack/hot/dev-server' ],
		prod = ['./app/index.js' ];

	return NODE_ENV == 'DEVELOP' ? dev.concat(prod) : prod;
}

function getPlugins() {
	var dev = [ new webpack.HotModuleReplacementPlugin() ], 
		prod = [];

	return NODE_ENV == 'DEVELOP' ? dev.concat(prod) : prod;
}

if(NODE_ENV == 'PRODUCTION') {
	config.plugins.push(
		new webpack.optimize.UglifyJsPlugin({
			compress: {
				warnings: false,
				drop_console: true,
				unsafe: true
			}
		})
	)
}

module.exports = config;
