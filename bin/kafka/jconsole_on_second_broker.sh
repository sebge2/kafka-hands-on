#!/bin/sh

PARAMETERS="kafka-broker-2:29999"

if [ "$JDK_8_HOME" ]; then
  eval "$JDK_8_HOME/bin/jconsole $PARAMETERS"
else
  eval "jconsole $PARAMETERS"
fi