#!/bin/sh
if [ -z "$JAVA_HOME" ] || [ ! -x "$JAVA_HOME/bin/java" ]; then
  JAVA_HOME="$(java -XshowSettings:properties -version 2>&1 | awk -F'= ' '/java.home/ {print $2; exit}')"
  export JAVA_HOME
fi
exec mvn "$@"
