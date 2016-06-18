module.exports = function(grunt) {
    grunt.initConfig({
        concat: {
            options: {
                separator: ';',
            },
            dist: {
                src: [
                    'src/main/resources/static/app/*.js'
                ],
                dest: 'src/main/resources/static/app/dist/auth.js'
            }
        },
        uglify: {
            dist: {
                src: 'src/main/resources/static/app/dist/auth.js',
                dest: 'src/main/resources/static/app/dist/auth.min.js'
            }
        }
    });
    grunt.registerTask('default', ['concat','uglify']);
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
}