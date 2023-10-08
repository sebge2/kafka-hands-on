#!/bin/bash

execute_script() {
  FILE_NAME=$1
  SCRIPT=$(cat "$FILE_NAME"| tr -d '\n' | tr -d '\r')

  RESULT=$(curl -s -X "POST" "http://localhost:8088/ksql" -H "Content-Type: application/vnd.ksql.v1+json; charset=utf-8"  -d '{"ksql":"'"$SCRIPT"'"}')
  RET=$?

  if [[ "$RESULT" =~ .*"A stream with the same name already exists".* ]]
  then
    echo "Stream already exists [$FILE_NAME]"
  elif [[ "$RESULT" =~ .*"A table with the same name already exists".* ]]
  then
    echo "Table already exists [$FILE_NAME]"
  elif [[ "$RESULT" =~ .*"Stream created".* ]]
  then
    echo "Stream created [$FILE_NAME]"
  elif [[ "$RESULT" =~ .*"Table created".* ]]
  then
    echo "Table created [$FILE_NAME]"
  else
    echo "Could not execute script [$FILE_NAME]"
    echo "Exit code: $RET"
    echo "Response: $RESULT"
    #exit 1
  fi
}

FILES="/ksql/sample-queries/*"
for f in $FILES
do
  execute_script "$f"
done