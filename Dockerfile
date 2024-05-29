FROM openjdk:17-jdk-alpine

# Tạo thư mục làm việc trong container
WORKDIR /app

# Sao chép file jar vào thư mục làm việc
COPY target/exe201.jar /app/

# Kiểm tra sự tồn tại của file exe201.jar sau khi sao chép
RUN echo "Checking if exe201.jar exists in /app:" && ls -l /app/exe201.jar

# Sao chép các thư viện bổ sung vào thư mục libs
COPY libs/payos.jar /app/libs/

# Kiểm tra sự tồn tại của file payos.jar sau khi sao chép
RUN echo "Checking if payos.jar exists in /app/libs:" && ls -l /app/libs/payos.jar

# Expose port 8080
EXPOSE 8080

# Chạy ứng dụng
CMD ["java", "-cp", "/app/exe201.jar:/app/libs/*", "com.example.exe202backend.Exe202BackendApplication"]
