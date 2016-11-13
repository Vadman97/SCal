# CS 201 Final Project - USCalendar

- Samuel Breck breck@usc.edu 29909
- Prachi Nawathe pnawathe@usc.edu 29909
- Clifford Lee leecliff@usc.edu 29909
- Gautam Paranjape gparanja@usc.edu 29909
- Vadim Korolik korolik@usc.edu 29909

# Calendar Branch
## How to run
Build system is using bower and npm to manage certain dependencies. `bower.json` and `package.json` have been include so to build, just run: `npm install` and `bower install` and you should be good.

Using Gulp as a build system. Gulpfile is currently set up to monitor all changes in .html files and .scss files. On changing either, browser will be reloaded.

Gulp serves files to port 3000. To view the page, go to `localhost:3000`

## Dependencies
FullCalendar
AngularJS
jQuery
Moment.js
