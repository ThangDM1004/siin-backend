FROM openjdk:17-jdk-alpine

WORKDIR /app

# Sao chép các file jar vào thư mục /app/libs
COPY target/exe201.jar /app/
COPY libs/payos.jar /app/libs/

# CMD để chạy ứng dụng Spring Boot
CMD ["java", "-cp", "/app/exe201.jar:/app/libs/payos.jar", "org.springframework.boot.loader.JarLauncher"]
