var gulp			= require('gulp');
var sass			= require('gulp-sass');
var concat			= require('gulp-concat');
var autoPrefixer	= require('gulp-autoprefixer');
var browserSync		= require('browser-sync');

// 'sass' task
// -- compiles styles.scss into css
// -- styles.scss contains imports of all other necessary stylesheets
gulp.task('sass', function() {
	return gulp.src('assets/scss/styles.scss')
	.pipe(sass({style: 'expanded'}))
	.pipe(autoPrefixer('last 2 version', 'safari 5', 'ie 8', 'ie 9', 'opera 12.1', 'ios 6'))
	.pipe(gulp.dest('assets/css'))
	.pipe(browserSync.reload({stream: true}));
});

// 'serve' task
// -- serves files from root directory
// -- also watches files/directories for live uploading during development
gulp.task('serve', ['sass'], function() {
	browserSync.init({
		server: {
			basedir: "./"
		}
	});

	gulp.watch('assets/scss/*.scss', ['sass']);
	gulp.watch('*.html').on('change', browserSync.reload);
});

// 'default' task
gulp.task('default', ['serve']);
