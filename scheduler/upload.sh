#!/usr/bin/env bash

./gradlew build
scp build/libs/datapipeline-scheduler-0.1.0-SNAPSHOT.jar tkw@47.103.34.244:~/jar
