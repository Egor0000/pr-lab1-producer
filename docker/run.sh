#!/usr/bin/env bash

export LAB1_PRODUCER_HOME=/app/pr-lab1-producer
export LAB1_PRODUCER_VERSION=ideaprojects-producer:latest
export LAB1_PRODUCER_PROFILE=default
export PORT=8075
export CONSUMER_PORT=8085
export APP_NAME=producer-dev
export ADDRESS=localhost

function noArgumentSupplied() {

    echo ""
    echo "========================================================================="
    echo ""

    exit 1
}

args=("$@")

echo "========================================================================="
echo ""
echo "  LAB1_PRODUCER Docker Environment"
echo ""
echo "  Number of arguments: $#"

if [[ $# -eq 0 ]] ; then
    echo '  No arguments supplied'
    noArgumentSupplied
    exit 1
fi

if [[ -z ${args[0]} ]] ; then
    echo '  no example instance name supplied'
    noArgumentSupplied
    exit 1
fi

export LAB1_PRODUCER_INSTANCE_NAME=${args[0]}

echo "  LAB1_PRODUCER instance name: ${LAB1_PRODUCER_INSTANCE_NAME}"
echo "  LAB1_PRODUCER profile: ${LAB1_PRODUCER_PROFILE}"
echo ""
echo "========================================================================="
echo ""

# -e JAVA_OPTS="${JAVA_OPTS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8787" \

docker run -it --name ${LAB1_PRODUCER_INSTANCE_NAME} --net host --log-driver none \
-e SPRING_PROFILES_ACTIVE=${LAB1_PRODUCER_PROFILE} \
-e ADDRESS=${ADDRESS} \
-e PORT=${PORT} \
-e CONSUMER_PORT=${CONSUMER_PORT} \
-e APP_NAME=${APP_NAME} \
-e JAVA_OPTS="${JAVA_OPTS}" \
--rm ${LAB1_PRODUCER_VERSION}