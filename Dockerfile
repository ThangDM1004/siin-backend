FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/exe201.jar /app/
COPY libs/payos.jar /app/libs/
EXPOSE 8080
CMD ["java", "-cp", "/app/exe201.jar:/app/libs/payos.jar","org.springframework.boot.loader.launch.JarLauncher"]
