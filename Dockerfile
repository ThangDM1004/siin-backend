FROM openjdk:17
WORKDIR /app
COPY /target/exe201.jar exe201.jar
EXPOSE 8080
CMD ["java", "-jar", "exe201.jar"]