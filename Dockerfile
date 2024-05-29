FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/exe201.jar /app
COPY libs/payos.jar /app/libs/payos.jar
EXPOSE 8080
CMD ["java", "-Dloader.path=/app/libs", "-jar", "exe201.jar"]