#!/bin/sh

PARAMETERS="kafka-broker-1:19999"

if [ "$JDK_8_HOME" ]; then
  eval "$JDK_8_HOME/bin/jconsole $PARAMETERS"
else
  eval "jconsole $PARAMETERS"
fi