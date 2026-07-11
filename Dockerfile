FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM amazoncorretto:21-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
