#!/bin/bash
#
# start ws service
#
###EOF

. /etc/profile
. ~/.profile

param=$1

prog=console-websocket

export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))
pwd=$(cd `dirname $0`; pwd)
export DEPLOY_PATH=${pwd%/*}
export DEBUG_PORT=5283

if [ ! -d $JAVA_HOME ];then
    echo "please set right JAVA_HOME in this file"
    exit 0
fi

if [ ! -d $DEPLOY_PATH ];then
    echo "please set right DEPLOY_PATH in this file"
    exit 0
fi

function usage(){
cat << EOF
Usage: startServer.sh --port <port>  [options]

Options:
    --help | -h Print usage information.
    --port | -p Set java debug port, default value(5100).
EOF

exit $1
}

while [ $# -gt 0 ]; do
    case "$1" in
        -h|--help) usage 0 ;;
        -p|--port) shift; DEBUG_PORT=$1 ;;
        *) shift ;; # ignore
    esac
    shift
done


export JAVA_OPTIONS="-Xmx512m -Xms256m"
export CLASSPATH=.:${CLASSPATH}:${DEPLOY_PATH}/resources:${DEPLOY_PATH}/lib

export JAVA_DEBUG="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=n"

${JAVA_HOME}/bin/java -cp ${DEPLOY_PATH}/resources:${DEPLOY_PATH}/lib/*:${DEPLOY_PATH}/lib/Console-Gateway-1.0.jar:. ${JAVA_OPTIONS} ${JAVA_DEBUG} io.vertx.core.Launcher run "com.iov.websocket.server.verticles.StartWebSocketVerticle" -cluster &


