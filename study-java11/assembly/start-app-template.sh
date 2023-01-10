#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

function start_app() {
    javaPid=

    mkdir -p /data/app/logs/${app.name}/
    
    while /bin/true ; do
    cd $DIR
    java -DLOG_PATH=/data/api-process/logs/${app.name}/ \
        -Xms${min.heap} \
        -Xmx${max.heap} \
        -jar ${app.name}-${project.version}.${project.packaging} >> /data/app/logs/${app.name}/stdout.log 2>&1 &

    javaPid=$!

    trap "echo 'killing $javaPid' ; kill $javaPid; exit 0" SIGINT SIGTERM

    echo "Java Process started: pid $javaPid"

    wait $javaPid

    done
}

ulimit -u 6144
start_app &
