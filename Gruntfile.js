module.exports = function (grunt) {

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-terser');

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
					{src: 'src/main/resources/META-INF/resources/js/tastatursteuerung.js', dest: 'www/tastatursteuerung.debug.js'},
                    {src: 'src/main/resources/META-INF/resources/js/sidebar-collapse.js', dest: 'www/sidebar-collapse.debug.js'}
				]
			},

            toproject: {
                files: [
                    {cwd: 'www/', expand: true, src: ['*.js', '*.js.map', '!*.debug.js'], dest: 'target/classes/META-INF/resources/js/'},
                    {cwd: 'www/', expand: true, src: ['*.debug.js'], dest: 'target/classes/META-INF/resources/js/www'},
                    {src: 'www/specialcharpicker.css', dest: 'target/classes/META-INF/resources/css/specialcharpicker.css'},
                    {src: 'www/charpickerdinspec.css', dest: 'target/classes/META-INF/resources/css/charpickerdinspec.css'}
                ]
            }
		},

        // terser for minifying
        terser: {
            js: {
                files: {'www/onload.js':['www/onload.debug.js']},
                //sourceMap option is needed to include sourceMappingURL in the minified file
                options: {sourceMap: {url: 'onload.js.map'}}
            },
            picker: {
                files: {'www/specialcharpicker.js':['www/specialcharpicker.debug.js']},
                options: {sourceMap: {url: 'specialcharpicker.js.map'}}
            },
            collapse: {
                files: {'www/sidebar-collapse.js':['www/sidebar-collapse.debug.js']},
                options: {sourceMap: {url: 'sidebar-collapse.js.map'}}
            },
			tastatur: {
                files: {'www/tastatursteuerung.js':['www/tastatursteuerung.debug.js']},
                options: {sourceMap: {url: 'tastatursteuerung.js.map'}}
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
            },
            charpickerdinspec: {
                options: {
                    cleancss: true,
                    sourceMap: false
                },
                files: {
                    "www/charpickerdinspec.css": "src/main/resources/grunt/css/charpickerdinspec.css"
                }
            }
        }
		
    });
	
    grunt.registerTask('default', ['clean', 'jshint', 'copy:towww','terser','less' ,'copy:toproject']);

};