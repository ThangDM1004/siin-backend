FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/exe201.jar /app/exe201.jar
COPY libs/payos.jar /app/libs/payos.jar

CMD ["java", "-jar", "/app/exe201.jar"]
