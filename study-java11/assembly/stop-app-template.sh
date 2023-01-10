#!/bin/bash

PID=`pgrep -f start-${app.name}.sh | xargs`

if ! [ -z "$PID" ]; then
    echo "Terminate start scripts to stop ${app.name}"
    kill $PID
    sleep 2
fi

PID=$(pgrep -f 'jar ${app.name}' | xargs)' '$(pgrep -f start-${app.name}.sh | xargs)
if ! [ -z "$PID" ]; then
    echo "PIDs still running: $PID"
    echo "Giving them a little more time..."
    sleep 10
    echo "Forcefully Killing all remaining processes."
    kill -9 $PID
fi