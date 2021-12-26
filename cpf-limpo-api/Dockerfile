FROM gradle:6.9.0-jdk11 AS build
COPY --chown=gradle:gradle . /usr/cpf-limpo-api/src
WORKDIR /usr/cpf-limpo-api/src
RUN gradle build -x test --no-daemon
CMD ["java", "-jar", "build/libs/cpf-limpo-api-0.0.1-SNAPSHOT.jar"]