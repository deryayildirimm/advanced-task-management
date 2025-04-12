# 1️⃣ Java 21 JDK içeren base image
FROM maven:3.9.6-eclipse-temurin-21 AS build

# 2️⃣ Uygulamanın çalışacağı klasörü ayarla
WORKDIR /app

# 3️⃣ Projedeki her şeyi container içine kopyala
COPY . .

# 4️⃣ Maven ile projeyi derle (testleri atlıyoruz)
RUN mvn clean package -DskipTests

# 5️⃣ Oluşan jar dosyasını çalıştır (jar ismini POM’dan teyit et)
CMD ["java", "-jar", "target/advanced-task-management-0.0.1-SNAPSHOT.jar"]
