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

		// Copy
		copy: {
            toproject: {
                files: [
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
	
    grunt.registerTask('default', ['clean', 'less' ,'copy:toproject']);

};