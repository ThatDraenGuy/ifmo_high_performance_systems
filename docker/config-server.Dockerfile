FROM maven:latest AS build
WORKDIR /app
COPY config-server/pom.xml config-server/pom.xml
COPY pom.xml .

WORKDIR /app/config-server
RUN --mount=type=cache,target=/root/.m2 mvn verify clean --fail-never
COPY config-server/src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM openjdk:24-jdk-slim

WORKDIR /app
COPY --from=build /app/config-server/target/*.jar app.jar
EXPOSE 8800
ENTRYPOINT ["java", "-jar", "app.jar"]