# Stage 1: Build the application
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/exe201.jar /app/exe201.jar
COPY --from=build /app/libs/payos.jar /app/libs/payos.jar
EXPOSE 8080
CMD ["java", "-jar", "exe201.jar"]
