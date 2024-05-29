FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/exe201.jar /app/
COPY libs/payos.jar /app/libs/

RUN echo "Checking if exe201.jar exists in /app:" && ls -l /app/exe201.jar
RUN jar tf /app/exe201.jar | grep Exe202BackendApplication
RUN echo "Checking manifest file in /app/exe201.jar:" && unzip -p /app/exe201.jar META-INF/MANIFEST.MF

EXPOSE 8080

CMD ["java", "-Dloader.path=/app/libs", "-jar", "/app/exe201.jar"]
