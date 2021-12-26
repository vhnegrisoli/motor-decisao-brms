FROM gradle:6.9.0-jdk11 AS build
COPY --chown=gradle:gradle . /usr/motor-decisao-api/src
WORKDIR /usr/motor-decisao-api/src
RUN gradle build -x test --no-daemon
CMD ["java", "-jar", "build/libs/motor-decisao-0.0.1-SNAPSHOT.jar"]