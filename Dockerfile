FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/exe201.jar /app/
COPY libs/payos.jar /app/libs/
EXPOSE 8080
CMD ["java", "-Dloader.path=/app/libs/*", "-jar", "/app/exe201.jar"]
