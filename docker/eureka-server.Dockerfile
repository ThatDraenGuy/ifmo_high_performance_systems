FROM maven:latest AS build
WORKDIR /app
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY pom.xml .

WORKDIR /app/eureka-server
RUN --mount=type=cache,target=/root/.m2 mvn verify clean --fail-never
COPY eureka-server/src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM openjdk:24-jdk-slim

WORKDIR /app
COPY --from=build /app/eureka-server/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]