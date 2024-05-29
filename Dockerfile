FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy application jar
COPY target/exe201.jar /app

# Copy dependency jar
COPY libs/payos.jar /app/libs/payos.jar

# If there are other dependencies, copy them too
# COPY path/to/other/dependencies/*.jar /app/libs/

EXPOSE 8080

# Set the classpath and run the application
CMD ["java", "-cp", "/app/exe201.jar:/app/libs/payos.jar", "com.example.exe202backend.Exe202BackendApplication"]