@ECHO OFF

@REM **********************************************************************************************
@REM This script is used to set properties for starting PAAS
@REM This script initializes the following variables before calling commEnv to set other variables:
@REM 
@REM DEPLOY_PATH     - The deployed directory of your PAAS standalone installation.
@REM JAVA_VM         - The desired Java VM to use. 
@REM JAVA_HOME       - Location of the version of Java used to start PAAS application
@REM 
@REM JAVA_OPTIONS    - Java command-line options for running the server. (These
@REM                   will be tagged on to the end of the JAVA_VM)
@REM MIDDLEWARE	     - The directory of the embedded middleware for running CONSOLE application.
@REM		       Strongly recommend that users do not change the default value
@REM CLASSPATH	     - The classpath to run start procedure
@REM **********************************************************************************************

@echo Set PAAS Environment

@set DEPLOY_PATH=%cd%/../
@echo %DEPLOY_PATH%

@set JAVA_HOME=$JAVA_HOME

@set JAVA_VM=-XX:PermSize=128M -XX:MaxNewSize=512m -XX:MaxPermSize=256m -Djava.awt.headless=true -Xmx512m -Xms256m

@set JAVA_OPTIONS=

@echo java version
@echo .
%JAVA_HOME%/bin/java -version
@echo .
@unset %classpath%
@set classpath=%classpath%;.;%JAVA_HOME%/jre/lib/rt.jar;%DEPLOY_PATH%/resources;%DEPLOY_PATH%/lib
@echo %classpath%

