FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/exe201.jar /app/exe201.jar
CMD ["java", "-jar", "/app/exe201.jar"]
HEALTHCHECK --interval=120s --timeout=10s \
CMD curl -f http://172.171.207.227:8080/swagger-ui/index.html || exit 1
