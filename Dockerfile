FROM amazoncorretto:17-alpine-jdk

# Установка Maven
RUN apk add --no-cache maven

# Копирование исходного кода в контейнер
COPY . /app

# Установка рабочей директории
WORKDIR /app

# Сборка приложения с помощью Maven без выполнения тестов
RUN mvn package -DskipTests=true

# Указание точки входа для контейнера
ENTRYPOINT ["java", "-jar", "/app/target/e-shop-0.0.1-SNAPSHOT.jar"]