FROM adoptopenjdk/openjdk11:jdk-11.0.11_9

WORKDIR /usr/src/myapp

ARG PROJECT_NAME

# 复制配置文件
ADD src/main/resources/remote/ ./

# 复制jar
ADD target/daily-*.jar ./
ADD target/lib ./lib

VOLUME /usr/src/myapp/logs

ENV JAVA_OPTS='-Xms64m -Xmx256m -Dfile.encoding=UTF-8'

RUN echo "#!/bin/bash"> run.sh && chmod +x run.sh
RUN echo "java -jar -Dlogback.configurationFile=logback.xml \${JAVA_OPTS} daily-*.jar" >> run.sh

CMD ["./run.sh"]