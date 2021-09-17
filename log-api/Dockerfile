FROM gradle:6.9.0-jdk11 AS build
COPY --chown=gradle:gradle . /usr/log-api/src
WORKDIR /usr/log-api/src
RUN gradle build
CMD ["gradle", "bootRun"]