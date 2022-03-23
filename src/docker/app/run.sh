#!/bin/sh

LIBS="$( find /opt/app/lib/ -name \*.jar -print0 | tr '\0' ':' )"

java \
  ${JVM_OPTS} \
  -Dspring.profiles.active="${SPRING_PROFILE}" \
  -cp "/opt/app/conf/:$LIBS" \
  "${APP_MAIN_CLASS}"
