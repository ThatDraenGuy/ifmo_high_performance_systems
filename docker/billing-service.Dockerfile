FROM maven:3.9.9-amazoncorretto-21-alpine AS build

WORKDIR /app

COPY billing-service/pom.xml billing-service/pom.xml
COPY common common
COPY pom.xml .

WORKDIR /app/common
RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests


WORKDIR /app/billing-service
RUN --mount=type=cache,target=/root/.m2 mvn verify clean --fail-never
COPY billing-service/src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM amazoncorretto:21-alpine

WORKDIR /app
COPY --from=build /app/billing-service/target/*.jar app.jar
EXPOSE 8800
ENTRYPOINT ["java", "-jar", "app.jar"]