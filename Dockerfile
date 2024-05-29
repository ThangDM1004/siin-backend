# Stage 1: Build stage
FROM maven:3.8.4 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:resolve
COPY src/ /app/src/
RUN mvn -B package

# Stage 2: Run stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/exe201.jar /app/exe201.jar
COPY --from=build /app/libs/payos.jar /app/libs/payos.jar
EXPOSE 8080
CMD ["java", "-cp", "/app/exe201.jar:/app/libs/payos.jar", "com.example.exe202backend.Exe202BackendApplication"]
