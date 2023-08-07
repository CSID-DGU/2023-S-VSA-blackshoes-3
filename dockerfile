# Base image 설정
FROM openjdk:11-jdk

# 앱을 실행할 디렉토리를 설정합니다
WORKDIR /app

# 스프링 애플리케이션의 jar 파일을 /app 디렉토리로 복사합니다
COPY build/libs/user-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8001

CMD ["java", "-jar", "app.jar"]
