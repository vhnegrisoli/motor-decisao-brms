FROM gradle:6.9.0-jdk11 AS build
COPY --chown=gradle:gradle . /usr/cep-valido-api/src
WORKDIR /usr/cep-valido-api/src
RUN gradle build -x test --no-daemon
CMD ["java", "-jar", "build/libs/cep-valido-api-0.0.1-SNAPSHOT.jar"]