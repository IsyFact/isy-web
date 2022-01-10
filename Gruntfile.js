module.exports = function (grunt) {

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-less');

    grunt.initConfig({

        pkg: grunt.file.readJSON('package.json'),

        clean: {
            build: {
                src: ["www/*"]
            }
        },

        //static code analysis for es-specific js code
        jshint: {
            options: {
                browser: true,
                strict: true,
                esversion: 6,
                globals: {
                    jQuery: true,
                    angular: true
                }
            },
            all: ['src/main/resources/META-INF/resources/js/*.js']
        },
		
		// Copy
		copy: {
			towww: {
				files: [
					{src: 'src/main/resources/META-INF/resources/js/onload.js', dest: 'www/onload.debug.js'},
					{src: 'src/main/resources/META-INF/resources/js/specialcharpicker.js', dest: 'www/specialcharpicker.debug.js'},
                    {src: 'src/main/resources/META-INF/resources/js/charpickerdinspec.js', dest: 'www/charpickerdinspec.debug.js'},
					{src: 'src/main/resources/META-INF/resources/js/tastatursteuerung.js', dest: 'www/tastatursteuerung.debug.js'},
                    {src: 'src/main/resources/META-INF/resources/js/sidebar-collapse.js', dest: 'www/sidebar-collapse.debug.js'}
				]
			},

            toproject: {
                files: [
                    {src: 'www/onload.js', dest: 'target/classes/META-INF/resources/js/onload.js'},
                    {src: 'www/specialcharpicker.js',dest: 'target/classes/META-INF/resources/js/specialcharpicker.js'},
                    {src: 'www/charpickerdinspec.js',dest: 'target/classes/META-INF/resources/js/charpickerdinspec.js'},
                    {src: 'www/tastatursteuerung.js', dest: 'target/classes/META-INF/resources/js/tastatursteuerung.js'},
                    {src: 'www/sidebar-collapse.js', dest: 'target/classes/META-INF/resources/js/sidebar-collapse.js'},
                    {src: 'www/specialcharpicker.css', dest: 'target/classes/META-INF/resources/css/specialcharpicker.css'}
                ]
            }
		},

		less: {
            specialcharpicker: {
                options: {
                    cleancss: true,
                    sourceMap: false
                },
                files: {
                    "www/specialcharpicker.css": "src/main/resources/grunt/css/specialcharpicker.css"
                }
            }
        }
		
    });
	
    grunt.registerTask('default', ['clean', 'jshint', 'copy:towww','less','copy:toproject']);

};