# Stage 1: Build Spring Boot application
FROM maven:3.8.3-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Stage 2: Create final Docker image
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/exe201.jar /app/
EXPOSE 8080
CMD ["java", "-jar", "exe201.jar"]
