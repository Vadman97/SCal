#!/bin/bash
TOMCAT=tomcat8

# UPDATE MASTER BRANCH CODE
cd /var/lib/$TOMCAT/webapps/ROOT
git pull; 

# INSTALL FRONTEND DEPENDENCIES
npm install;
bower install;
gulp build;

# CONFIGURE BACKEND DEPENDENCIES
cp lib/* /usr/share/$TOMCAT/lib

# COMPILE JAVA
cd WEB-INF; 
rm -rf classes;
mkdir classes;
shopt -s globstar;
javac -cp /usr/share/$TOMCAT/lib/\*:/usr/share/java/mysql.jar -d classes src/**/*.java;
