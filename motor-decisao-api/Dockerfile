FROM gradle:6.9.0-jdk11 AS build
COPY --chown=gradle:gradle . /usr/motor-decisao-api/src
WORKDIR /usr/motor-decisao-api/src
RUN gradle build
CMD ["gradle", "bootRun"]