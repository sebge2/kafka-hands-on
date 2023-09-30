#!/bin/sh

PROJECT="kafka"

docker-compose -p $PROJECT up -d --remove-orphans
