FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy the main jar file
COPY target/exe201.jar /app/

# Copy the libraries
COPY libs/payos.jar /app/libs/

# Check if the JAR files exist and list their contents
RUN echo "Checking if exe201.jar exists in /app:" && ls -l /app/exe201.jar
RUN jar tf /app/exe201.jar | grep Exe202BackendApplication
RUN echo "Checking manifest file in /app/exe201.jar:" && unzip -p /app/exe201.jar META-INF/MANIFEST.MF
RUN echo "Checking if payos.jar exists in /app/libs:" && ls -l /app/libs/payos.jar

# Expose the port that the service runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-Dloader.path=/app/libs", "-Dloader.main=com.example.exe202backend.Exe202BackendApplication", "-jar", "/app/exe201.jar"]
