FROM openjdk:11

WORKDIR /usr/src/myapp

ARG JAR_FILE

ADD target/${JAR_FILE} ./

ADD src/main/resources/application.remote.yml ./application.yml
ADD src/main/resources/logback.remote.xml ./logback.xml

VOLUME /usr/src/myapp/logs

ENV JAVA_OPTS='-Xms64m -Xmx128m -Dfile.encoding=UTF-8'

RUN echo "#!/bin/bash"> run.sh && chmod +x run.sh
RUN echo "java -jar \${JAVA_OPTS} ${JAR_FILE}" >> run.sh

CMD ["./run.sh"]