FROM gradle:6.9.0-jdk11 AS build
COPY --chown=gradle:gradle . /usr/cep-valido-api/src
WORKDIR /usr/cep-valido-api/src
RUN gradle build
CMD ["gradle", "bootRun"]