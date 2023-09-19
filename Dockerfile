FROM openjdk:11

WORKDIR /app
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

RUN ./gradlew --no-daemon dependencies
COPY src ./src

RUN ./gradlew --no-daemon build -x test
RUN mkdir -p target && (cd build/libs && cp content-slave-service-0.0.1-SNAPSHOT.jar ../../target/content-slave-service.jar)

CMD ["java", "-jar", "target/content-slave-service.jar"]
EXPOSE 8011