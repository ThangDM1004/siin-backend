FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/exe201.jar /app
EXPOSE 80
CMD ["java", "-jar", "exe201.jar"]