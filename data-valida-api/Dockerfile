FROM gradle:6.9.0-jdk11 AS build
COPY --chown=gradle:gradle . /usr/data-valida-api/src
WORKDIR /usr/data-valida-api/src
RUN gradle build -x test --no-daemon
CMD ["java", "-jar", "build/libs/data-valida-api-0.0.1-SNAPSHOT.jar"]