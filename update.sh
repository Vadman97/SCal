#!/bin/bash

# UPDATE MASTER BRANCH CODE
cd /var/lib/tomcat8/webapps/ROOT
git pull; 

# INSTALL FRONTEND DEPENDENCIES
npm install;
bower install;
gulp build;

# COMPILE JAVA
cd WEB-INF; 
shopt -s globstar;
javac -cp /usr/share/tomcat8/lib/\*:/usr/share/java/mysql.jar -d classes src/**/*.java;
