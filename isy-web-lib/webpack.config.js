const path = require('path');
const ESLintPlugin = require('eslint-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');
const TerserPlugin = require("terser-webpack-plugin");
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
    target:  ['es5', 'web'],
    entry: {
        isyweb: [
            './src/main/js/main.js',
            './src/main/css/specialcharpicker.css'
        ]
    },
    // js bundle
    output: {
        filename: 'js/[name].bundle.js',
        path: path.resolve(__dirname, 'target', 'classes', 'META-INF', 'resources')
    },
    // config for css minimization
    module: {
        rules: [
            {
                test: /fonts\/[^/]+\.(woff(2)?|ttf|eot|svg)(\?v=\d+\.\d+\.\d+)?$/,
                type: 'asset/resource',
                generator: {
                    filename: '[name][ext]',
                    publicPath: '../fonts/',
                    outputPath: 'fonts/',
                }
            },
            {
                test: /webfonts\/[^/]+\.(woff(2)?|ttf|eot|svg)(\?v=\d+\.\d+\.\d+)?$/,
                type: 'asset/resource',
                generator: {
                    filename: '[name][ext]',
                    publicPath: '../webfonts/',
                    outputPath: 'webfonts/',
                }
            },
            {
                test: /img\/[^/]+\.(png|gif)$/,
                type: 'asset/resource',
                generator: {
                    filename: '[name][ext]',
                    publicPath: '../img/',
                    outputPath: 'img/',
                }
            },
            {
                test: /img\/controls\/[^/]+\.(png|gif)$/,
                type: 'asset/resource',
                generator: {
                    filename: '[name][ext]',
                    publicPath: '../img/controls/',
                    outputPath: 'img/controls/',
                }
            },
            {
                test: /\.css$/i,
                use: [MiniCssExtractPlugin.loader, 'css-loader'],
            }
        ]
    },
    optimization: {
        minimizer: [
            new TerserPlugin({
                terserOptions: {
                    ecma: 5
                },
            }),
            new CssMinimizerPlugin(),
        ],
    },
    plugins: [
        new ESLintPlugin(),
        new MiniCssExtractPlugin({
            filename: "/css/[name].css"
        }),
        new CopyWebpackPlugin({
            "patterns": [
            {
                from: 'node_modules/jquery/dist/jquery.min.js',
                to: path.resolve(__dirname, 'target', 'classes', 'META-INF', 'resources', 'lib')
            },
            {
                from: 'node_modules/bootstrap/dist/js/bootstrap.min.js',
                to: path.resolve(__dirname, 'target', 'classes', 'META-INF', 'resources', 'lib')
            },
            {
                from: 'node_modules/magnific-popup/dist/jquery.magnific-popup.min.js',
                to: path.resolve(__dirname, 'target', 'classes', 'META-INF', 'resources', 'plugins', 'magnific-popup.min.js')
            },
            {
                from: 'node_modules/jquery-hotkeys/jquery-hotkeys.js',
                to: path.resolve(__dirname, 'target', 'classes', 'META-INF', 'resources', 'plugins', 'jquery.hotkeys.js')
            },
            {
                from: 'node_modules/jquery.inputmask/dist/jquery.inputmask.bundle.js',
                to: path.resolve(__dirname, 'target', 'classes', 'META-INF', 'resources', 'plugins', 'jquery.inputmask.bundle.js')
            },
            {
                from: 'node_modules/jquery.maskedinput/src/jquery.maskedinput.js',
                to: path.resolve(__dirname, 'target', 'classes', 'META-INF', 'resources', 'plugins', 'jquery.maskedinput.js')
            },
            {
                from: 'node_modules/jquery-visible/jquery.visible.min.js',
                to: path.resolve(__dirname, 'target', 'classes', 'META-INF', 'resources', 'plugins', 'jquery.visible.min.js')
            }
            ]
        })
    ]
};
