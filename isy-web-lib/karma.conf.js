module.exports = function (config) {
    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: '',


        // frameworks to use
        // available frameworks: https://www.npmjs.com/search?q=keywords:karma-adapter
        frameworks: ['jasmine', 'webpack'],


        // list of files / patterns to load in the browser
        files: [
            // jquery first, since it should be available when adding bootstrap
            "node_modules/@isyfact/isy-style/dist/lib/jquery.min.js",
            "node_modules/@isyfact/isy-style/dist/lib/bootstrap.min.js",
            "node_modules/@isyfact/isy-style/dist/lib/modernizr.js",
            "node_modules/@isyfact/isy-style/dist/plugins/*.js",
            {'pattern': 'src/test/js/**/*.spec.js', 'watched': false}
        ],


        // list of files / patterns to exclude
        exclude: [],


        // preprocess matching files before serving them to the browser
        // available preprocessors: https://www.npmjs.com/search?q=keywords:karma-preprocessor
        preprocessors: {
            'src/test/js/**/*.spec.js': ['webpack', 'sourcemap']
        },

        webpack: {
            // karma watches the test entry points
            // Do NOT specify the entry option
            // webpack watches dependencies

            // webpack configuration
            devtool: 'inline-source-map',
            watch: true
        },

        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://www.npmjs.com/search?q=keywords:karma-reporter
        reporters: ['progress', 'junit'],

        junitReporter: {
            outputDir: 'target/surefire-reports'
        },


        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,


        // start these browsers
        // available browser launchers: https://www.npmjs.com/search?q=keywords:karma-launcher
        browsers: [
            //'Firefox',
            'ChromiumHeadless'
        ],


        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false,

        // Concurrency level
        // how many browser instances should be started simultaneously
        concurrency: Infinity
    });
};
