@title console-websocket

@REM *************************************************************************
@REM This script is used to start iSwapCenter.
@REM 
@REM System will load all jar-packages in 'lib' directory
@REM *************************************************************************


@REM call setEnv.cmd here
@call "setEnv.cmd" %*
@echo .

@set prog=console-websocket
@set param=%1%
@set JAVA_OPTIONS="-Dpaas.classpath=../lib"
@echo %JAVA_OPTIONS%


@REM Set debug options
set DEBUG_PORT=5183
set JAVA_DEBUG=-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=%DEBUG_PORT%,server=y,suspend=n
java -cp ../resources;../lib/*;../lib/Console-Websocket-1.0.jar; %JAVA_OPTIONS% %JAVA_DEBUG% com.iov.main.StartWebSocket %prog% %param%

@pause
