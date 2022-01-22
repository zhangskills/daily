#!/usr/bin/env bash

git pull

mvn clean package -DskipTests=true jib:dockerBuild

docker rm -f daily

docker run -d --name daily -it \
  -e JAVA_OPTS='-Xmx128m -Dfile.encoding=UTF-8' \
  -v /etc/localtime:/etc/localtime:ro \
  -v /etc/timezone:/etc/timezone:ro \
  -p 192.168.0.33:8080:8080 \
  daily:1.1-SNAPSHOT