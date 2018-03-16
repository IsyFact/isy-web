module.exports = function (grunt) {

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-compress');
	grunt.loadNpmTasks('grunt-write-bower-json');
	grunt.loadNpmTasks('grunt-rename');
	grunt.loadNpmTasks('grunt-artifactory-artifact');
	
    grunt.initConfig({

        pkg: grunt.file.readJSON('package.json'),
		
        //clean tmp and output directories
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
					{src: 'www/onload.js', dest: '../target/classes/META-INF/resources/js/onload.js'},
					{src: 'www/specialcharpicker.js',dest: '../target/classes/META-INF/resources/js/specialcharpicker.js'},
					{src: 'www/tastatursteuerung.js', dest: '../target/classes/META-INF/resources/js/tastatursteuerung.js'},
                    {src: 'www/sidebar-collapse.js', dest: '../target/classes/META-INF/resources/js/sidebar-collapse.js'},
					{src: 'www/styles.css', dest: '../src/main/resources/META-INF/resources/css/styles.css'}
				]
			}    			
		},

        // Uglify
        uglify: {
            options: {
                sourceMap: 'www/onload.map'
            },
            js: { 
                files: {
                    'www/onload.js':['www/onload.debug.js']
                }
            },
            picker: {
                files: {
                    'www/specialcharpicker.js':['www/specialcharpicker.debug.js']
                }
            },
            collapse: {
                files: {
                    'www/sidebar-collapse.js':['www/sidebar-collapse.debug.js']
                }
            },
			tastatur: {
                files: {
                    'www/tastatursteuerung.js':['www/tastatursteuerung.debug.js']
                }
            },
        },
		
		// Styles um CSS für Charpicker erweitern
		less: {
            production: {
                options: {
                    cleancss: true,
                    sourceMap: false
                },
                files: {
                    "www/styles.css": "src/main/resources/grunt/less/styles.less",
                }
            }
        },
		
    });
	
    grunt.registerTask('default', ['clean', 'jshint', 'copy:towww','uglify','less','copy:toproject','less']);

};