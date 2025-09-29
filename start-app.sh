#!/bin/sh
set -e

if [ "$ENABLE_STRESS_NG" = "true" ]; then
    echo ">>> Running stress-ng with args: $STRESS_ARGS"
    stress-ng $STRESS_ARGS &
fi

if [ "$ENABLE_TC" = "true" ]; then
    echo ">>> Adding packet loss: ${TC_LOSS_PERCENT}%"
    echo ">>> Adding packet delay: ${TC_DELAY_TIME}"
    tc qdisc add dev eth0 root netem loss ${TC_LOSS_PERCENT}% delay ${TC_DELAY_TIME}
fi

exec java -javaagent:opentelemetry-javaagent.jar -Dspring.config.location=${CONFIG_PATH} -jar user-service.war