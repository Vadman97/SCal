var gulp			= require('gulp');
var sass			= require('gulp-sass');
var concat			= require('gulp-concat');
var autoPrefixer	= require('gulp-autoprefixer');
var browserSync		= require('browser-sync');

// 'sass' task
// -- compiles *.scss into *.css
// -- *.scss files and their directory are ignored on git
gulp.task('sass', function() {
	return gulp.src('assets/scss/*.scss')
	.pipe(sass({style: 'expanded'}))
	.pipe(autoPrefixer('last 2 version', 'safari 5', 'ie 8', 'ie 9', 'opera 12.1', 'ios 6'))
	.pipe(gulp.dest('assets/css'))
	.pipe(browserSync.reload({stream: true}));
});

// 'concatCss' task
// -- takes compiled css from assets/css and concatenates into one master css files
// -- master css file named 'styles.css'
gulp.task('concatCss', ['sass'], function() {
	return gulp.src(['assets/css/*.css', '!assets/css/styles.css'])
	.pipe(concat('styles.css'))
	.pipe(gulp.dest('assets/css'))
	.pipe(browserSync.reload({stream: true}));
});

// 'serve' task
// -- serves files from root directory
// -- also watches files/directories for live uploading during development
gulp.task('serve', ['concatCss'], function() {
	browserSync.init({
		server: {
			basedir: "./"
		}
	});

	gulp.watch('assets/scss/*.scss', ['sass', 'concatCss']);
	gulp.watch('*.html').on('change', browserSync.reload);
});

// 'default' task
gulp.task('default', ['serve']);
