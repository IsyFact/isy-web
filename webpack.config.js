const ESLintPlugin = require('eslint-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');

module.exports = {
    entry: {
        isyweb: [
            './src/main/js/main.js',
            './src/main/css/specialcharpicker.css'
        ]
    },
    output: {
        filename: '[name].bundle.js',
        path: __dirname + '/target/classes/META-INF/resources/js'
    },
    module: {
        rules: [
            {
                test: /\.css$/i,
                use: [MiniCssExtractPlugin.loader, 'css-loader'],
            },
        ],
    },
    optimization: {
        minimizer: [
            new CssMinimizerPlugin(),
        ],
    },
    plugins: [new ESLintPlugin(), new MiniCssExtractPlugin({
        filename: "../css/[name].css"
    })]
};