# Chọn Base Image để xây dựng môi trường
FROM eclipse-temurin:17-jdk-alpine

# Định nghĩa biến (Variable) sử dụng trong quá trình Build
ARG FILE_JAR=target/demo-0.0.1-SNAPSHOT.jar
# Copy file từ máy thật (Host) vào trong ảnh (Image)
ADD ${FILE_JAR} api-service.jar

ENTRYPOINT ["java","-jar","api-service.jar"]

#cho phép truy xuất, kết nối giữa container và ứng dụng
EXPOSE 80


