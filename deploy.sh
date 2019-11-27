#!/usr/bin/env bash

git pull

mvn clean package -DskipTests=true dockerfile:build

docker run --rm --name daily \
  -it -v /etc/localtime:/etc/localtime:ro \
  -v /etc/timezone:/etc/timezone:ro \
  -p 192.168.0.33:8080:8080
  daily:1.0-SNAPSHOT