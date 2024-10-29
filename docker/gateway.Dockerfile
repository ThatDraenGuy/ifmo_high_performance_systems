FROM maven:3.9.9-amazoncorretto-21-alpine AS build
WORKDIR /app
COPY gateway/pom.xml gateway/pom.xml
COPY pom.xml .

WORKDIR /app/gateway
RUN --mount=type=cache,target=/root/.m2 mvn verify clean --fail-never
COPY gateway/src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM amazoncorretto:21-alpine

WORKDIR /app
COPY --from=build /app/gateway/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]