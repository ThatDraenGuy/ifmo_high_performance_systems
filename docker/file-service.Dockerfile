FROM maven:3.9.9-amazoncorretto-21-alpine AS build

WORKDIR /app

COPY file-service/pom.xml file-service/pom.xml
COPY common common
COPY pom.xml .

WORKDIR /app/common
RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests


WORKDIR /app/file-service
RUN --mount=type=cache,target=/root/.m2 mvn verify clean --fail-never
COPY file-service/src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM amazoncorretto:21-alpine

WORKDIR /app
COPY --from=build /app/file-service/target/*.jar app.jar
EXPOSE 8800
ENTRYPOINT ["java", "-jar", "app.jar"]