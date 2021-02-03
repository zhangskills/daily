FROM openjdk:11

WORKDIR /usr/src/myapp

ARG PROJECT_NAME

ADD target/${PROJECT_NAME}.jar ./
ADD target/lib ./lib

VOLUME /usr/src/myapp/logs

ENV JAVA_OPTS='-Xmx128m -Dfile.encoding=UTF-8'

RUN echo "#!/bin/bash"> run.sh && chmod +x run.sh
RUN echo "java -jar \${JAVA_OPTS} ${PROJECT_NAME}.jar" >> run.sh

CMD ["./run.sh"]