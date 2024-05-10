FROM amazoncorretto:17-alpine-jdk


ENV GHUB_CLIENT_ID=${GHUB_CLIENT_ID}
ENV GHUB_CLIENT_SECRET=${GHUB_CLIENT_SECRET}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV REDIS_PASSWORD=${REDIS_PASSWORD}

RUN apk add --no-cache maven

COPY . /app

WORKDIR /app

#ENV SPRING_PROFILES_ACTIVE=test
#
#RUN mvn test -Ptest


ENV SPRING_PROFILES_ACTIVE=local

RUN #mvn spring-boot:test-run;

RUN mvn package -DskipTests=true;

ENTRYPOINT ["java", "-jar", "/app/target/e-shop-0.0.1-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "/app/target/e-shop-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=local"]