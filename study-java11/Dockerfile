FROM amazoncorretto:11-alpine3.16-jdk
WORKDIR /tmp
ARG DOCKER_USER=demo
RUN addgroup -S $DOCKER_USER && adduser -S $DOCKER_USER -G $DOCKER_USER
USER $DOCKER_USER
ARG JAR_FILE
ARG MIN_HEAP
ARG MAX_HEAP
# JAVA_OPTS is for test here. Environmental variables shouldn't be bind with docker image itself
ENV JAVA_OPTS="${MIN_HEAP} ${MAX_HEAP}"
ADD ${JAR_FILE} app.jar
CMD ["java", "-version"]
ENTRYPOINT exec java ${JAVA_OPTS} -XshowSettings:vm -jar /tmp/app.jar "$0" "$@"