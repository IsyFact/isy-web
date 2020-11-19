module.exports = {
    entry: {
        main: './src/main/resources/META-INF/resources/js/onload.js'
    },
    output: {
        filename: 'onload.js',
        path: __dirname + '/target/classes/META-INF/resources/js'
    }
};