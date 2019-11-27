#!/usr/bin/env bash

git pull

mvn clean package -DskipTests=true dockerfile:build

docker run --rm --name daily -it \
  -e JAVA_OPTS='-Xms128m -Xmx512m -Dfile.encoding=UTF-8' \
  -v /etc/localtime:/etc/localtime:ro \
  -v /etc/timezone:/etc/timezone:ro \
  -p 192.168.0.33:8080:8080
  daily:1.0-SNAPSHOT