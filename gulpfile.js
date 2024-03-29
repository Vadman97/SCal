// Clifford Lee 2016
require('es6-promise').polyfill();
var gulp			= require('gulp');
var sass			= require('gulp-sass');
var concat			= require('gulp-concat');
var autoPrefixer	= require('gulp-autoprefixer');
var browserSync		= require('browser-sync');

// 'sass' task
// -- compiles styles.scss into css
// -- styles.scss contains imports of all other necessary stylesheets
gulp.task('sass', function() {
	return gulp.src(['assets/scss/styles.scss', '!assets/scss/fullcalendar-custom.scss'])
	.pipe(sass({style: 'expanded'}))
	.pipe(autoPrefixer('last 2 version', 'safari 5', 'ie 8', 'ie 9', 'opera 12.1', 'ios 6'))
	.pipe(gulp.dest('assets/css'))
	.pipe(browserSync.reload({stream: true}));
});

// 'fcSass' task
// -- Unique task for compiling custom full calendar scss file into its own css files
gulp.task('fcSass', function() {
	return gulp.src('assets/scss/fullcalendar-custom.scss')
	.pipe(sass({style: 'expanded'}))
	.pipe(autoPrefixer('last 2 version', 'safari 5', 'ie 8', 'ie 9', 'opera 12.1', 'ios 6'))
	.pipe(gulp.dest('assets/css'))
	.pipe(browserSync.reload({stream: true}));
});


// 'serve' task
// -- serves files from root directory
// -- also watches files/directories for live uploading during development
gulp.task('serve', ['sass', 'fcSass'], function() {
	browserSync.init({
		server: {
			basedir: "./"
		}
	});

	gulp.watch('assets/scss/**/*.scss', ['sass', 'fcSass']);
	gulp.watch('app/**/*.js').on('change', browserSync.reload);
	gulp.watch(['index.html', 'partials/**/*.html']).on('change', browserSync.reload);
});

// 'default' task
gulp.task('default', ['serve']);
gulp.task('build', ['sass', 'fcSass']);
