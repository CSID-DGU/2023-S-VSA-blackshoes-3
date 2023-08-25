FROM openjdk:11

RUN apt-get update && apt-get install -y ffmpeg

WORKDIR /app
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

RUN ./gradlew --no-daemon dependencies
COPY src ./src

RUN ./gradlew --no-daemon build -x test
RUN mkdir -p target && (cd build/libs && cp upload-service-0.0.1-SNAPSHOT.jar ../../target/upload-service.jar)

CMD ["java", "-jar", "target/upload-service.jar"]
EXPOSE 8021
