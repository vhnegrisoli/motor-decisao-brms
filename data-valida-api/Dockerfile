FROM gradle:6.9.0-jdk11 AS build
COPY --chown=gradle:gradle . /usr/data-valida-api/src
WORKDIR /usr/data-valida-api/src
RUN gradle build
CMD ["gradle", "bootRun"]